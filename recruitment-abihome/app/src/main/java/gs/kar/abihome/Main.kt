package gs.kar.abihome

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapter.Type
import gs.kar.abihome.databinding.ItemImageBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import org.kodein.di.generic.instance

/**
 * Main.kt contains the activity and fragments which are the Controllers / Presenters.
 *
 * Their task is to reflect any changes of the app state onto the views, as well as propagate any
 * user-caused UI events back in the form of Messages, hence closing the "loop".
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        container.adapter = SectionsPagerAdapter(supportFragmentManager)

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> ImagesFragment()
                else -> TappedImageFragment.newInstance(index = position)
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }

    class ImagesFragment : Fragment() {

        private val update: Update<Message, AbihomeState> by DI.instance()
        private val state: State<AbihomeState> by DI.instance()

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_images, container, false) as RecyclerView
            view.layoutManager = GridLayoutManager(context, 2)
            return view
        }

        override fun onStart() {
            super.onStart()
            bind(view as RecyclerView)
        }

        private fun bind(view: RecyclerView) {
            val imageType = Type<ItemImageBinding>(R.layout.item_image)
                    .onBind {
                        val url = it.binding.item?.url
                        if (url != null) Glide.with(view).load(url).into(it.itemView as ImageView)
                    }
                    .onClick {
                        val image = it.binding.item
                        if (image != null) update.send(OnSelectionChange(image))
                    }

            launch {
                val channel = state.subscribe()
                val abi = channel.receive()
                launch(UI) {
                    LastAdapter(abi.images, BR.item).map<Image>(imageType).into(view)
                }
                channel.cancel()
            }
        }

    }

    class TappedImageFragment : Fragment() {

        private val state: State<AbihomeState> by DI.instance()
        private var channel: ReceiveChannel<AbihomeState>? = null

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_tappedimage, container, false)
        }

        override fun onStart() {
            super.onStart()
            bind(view as ImageView)
        }

        private fun bind(view: ImageView) {
            channel = state.subscribe()
            launch {
                channel?.consumeEach {
                    val index = arguments?.getInt(KEY_INDEX) ?: 1
                    val image = it.tappedImages.getOrNull(index - 1)
                    if (image != null) launch(UI) { Glide.with(view).load(image.url).into(view) }
                }
            }
        }

        override fun onStop() {
            channel?.cancel()
            super.onStop()
        }

        companion object {
            private val KEY_INDEX = "index"

            fun newInstance(index: Int): TappedImageFragment {
                val fragment = TappedImageFragment()
                val args = Bundle()
                args.putInt(KEY_INDEX, index)
                fragment.arguments = args
                return fragment
            }
        }
    }
}


