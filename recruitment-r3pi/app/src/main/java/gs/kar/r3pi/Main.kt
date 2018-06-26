package gs.kar.r3pi

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapter.Type
import gs.kar.r3pi.databinding.ActivityDetailBinding
import gs.kar.r3pi.databinding.ItemVolumeBinding
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import org.kodein.di.generic.instance

/**
 * Main.kt contains the activities which are the Controllers / Presenters.
 *
 * Their task is to reflect any changes of the app state onto the views, as well as propagate any
 * user-caused UI events back in the form of Messages, hence closing the "loop".
 */

class MainActivity : Activity() {

    private val state: State<R3piState> by DI.instance()
    private val update: Update<Message, R3piState> by DI.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler: RecyclerView = findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        bind(recycler)
    }

    private fun bind(view: RecyclerView) {
        val volumeType = Type<ItemVolumeBinding>(R.layout.item_volume)
                .onBind {
                    val url = it.binding.item?.volumeInfo?.imageLinks?.lowQuality()
                    if (url != null) {
                        val v = it.binding.thumbnail
                        Glide.with(view).load(url).into(v)
                    }
                }
                .onClick {
                    val volume = it.binding.item
                    if (volume != null) update.send(OnTapped(volume))
                }

        launch {
            val observable = ObservableArrayList<Volume>()
            launch(UI) {
                LastAdapter(observable, BR.item).map<Volume>(volumeType).into(view)
            }
            val channel = state.subscribe()
            channel.consumeEach { r3pi ->
                val lastIndex = observable.size - 1
                if (lastIndex < 0) launch(UI) { observable.addAll(r3pi.volumes) }
                else with (r3pi.volumes) {
                    val last = observable.get(lastIndex)
                    val newIndex = indexOf(last) + 1
                    if (newIndex < size) launch(UI) { observable.addAll(subList(newIndex, size)) }
                }
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

    override fun onResume() {
        super.onResume()
        launch {
            val channel = state.subscribe()
            do {
                val r3pi = channel.receive()
                if (r3pi.detailVolume != null) launch(UI) {
                    startActivity(Intent(this@MainActivity, DetailActivity::class.java))
                }
            } while (r3pi.detailVolume == null)
            channel.cancel()
        }
    }
}

class DetailActivity : Activity() {

    private val state: State<R3piState> by DI.instance()
    private val update: Update<Message, R3piState> by DI.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        bind(binding)
    }

    private fun bind(binding: ActivityDetailBinding) = launch {
        val channel = state.subscribe()
        val r3pi = channel.receive()
        channel.cancel()

        if (r3pi.detailVolume != null) with(r3pi.detailVolume) {
            launch(UI) {
                binding.item = this@with
                val v = binding.thumbnail
                val url = volumeInfo.imageLinks?.highQuality()
                if (url != null) Glide.with(v).load(url).into(v)
            }
        }
        else launch(UI) { super.onBackPressed() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        update.send(OnBack())
    }
}
