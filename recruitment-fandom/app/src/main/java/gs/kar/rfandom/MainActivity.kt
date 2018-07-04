package gs.kar.rfandom

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapter.Type
import gs.kar.rfandom.databinding.ItemImageBinding
import gs.kar.rfandom.databinding.ItemTitleBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import org.kodein.di.generic.instance

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

    class WikisFragment : Fragment() {

        private val state: State<FandomState> by DI.instance()
        private val update: Update<Message, FandomState> by DI.instance()

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_wikis, container, false) as RecyclerView
            view.layoutManager = LinearLayoutManager(context)
            return view
        }

        override fun onStart() {
            super.onStart()
            bind(view as RecyclerView)
        }

        private fun bind(view: RecyclerView) {
            val titleType = Type<ItemTitleBinding>(R.layout.item_title)

            launch {
                val observable = ObservableArrayList<Wiki>()
                launch(UI) {
                    LastAdapter(observable, BR.item).map<Wiki>(titleType).into(view)
                }
                val channel = state.subscribe()
                channel.consumeEach { fandom ->
                    launch(UI) { observable.appendFrom(fandom.wikis) }
                }
            }

            view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!view.canScrollVertically(1)) {
                        update.send(OnNextPage())
                    }
                }
            })
        }

    }

    class ImagesFragment : Fragment() {

        private val state: State<FandomState> by DI.instance()
        private val update: Update<Message, FandomState> by DI.instance()

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
                        val url = it.binding.item?.image
                        if (url != null) Glide.with(view).load(url).into(it.binding.thumbnail)
                    }

            launch {
                val observable = ObservableArrayList<Wiki>()
                launch(UI) {
                    LastAdapter(observable, BR.item).map<Wiki>(imageType).into(view)
                }
                val channel = state.subscribe()
                channel.consumeEach { fandom ->
                    launch(UI) { observable.appendFrom(fandom.wikis) }
                }
            }

            view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!view.canScrollVertically(1)) {
                        update.send(OnNextPage())
                    }
                }
            })
        }

    }
}

