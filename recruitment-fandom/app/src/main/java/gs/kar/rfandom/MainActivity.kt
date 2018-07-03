package gs.kar.rfandom

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        viewpager.adapter = TabsPagerAdapter(supportFragmentManager)
        tablayout.setupWithViewPager(viewpager)
    }

    inner class TabsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> WikisFragment()
                else -> ImagesFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when(position) {
                0 -> "Titles"
                else -> "Images"
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }

    class ImagesFragment : Fragment() {

//        private val update: Update<Message, AbihomeState> by DI.instance()
//        private val state: State<AbihomeState> by DI.instance()

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
//            val imageType = Type<ItemImageBinding>(R.layout.item_image)
//                    .onBind {
//                        val url = it.binding.item?.url
//                        if (url != null) Glide.with(view).load(url).into(it.itemView as ImageView)
//                    }
//                    .onClick {
//                        val image = it.binding.item
//                        if (image != null) update.send(OnSelectionChange(image))
//                    }
//
//            launch {
//                val channel = state.subscribe()
//                val abi = channel.receive()
//                launch(UI) {
//                    LastAdapter(abi.images, BR.item).map<Image>(imageType).into(view)
//                }
//                channel.cancel()
//            }
        }

    }

    class WikisFragment : Fragment() {

//        private val update: Update<Message, AbihomeState> by DI.instance()
//        private val state: State<AbihomeState> by DI.instance()

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_wikis, container, false) as RecyclerView
            view.layoutManager = GridLayoutManager(context, 2)
            return view
        }

        override fun onStart() {
            super.onStart()
            bind(view as RecyclerView)
        }

        private fun bind(view: RecyclerView) {
//            val imageType = Type<ItemImageBinding>(R.layout.item_image)
//                    .onBind {
//                        val url = it.binding.item?.url
//                        if (url != null) Glide.with(view).load(url).into(it.itemView as ImageView)
//                    }
//                    .onClick {
//                        val image = it.binding.item
//                        if (image != null) update.send(OnSelectionChange(image))
//                    }
//
//            launch {
//                val channel = state.subscribe()
//                val abi = channel.receive()
//                launch(UI) {
//                    LastAdapter(abi.images, BR.item).map<Image>(imageType).into(view)
//                }
//                channel.cancel()
//            }
        }

    }
}
