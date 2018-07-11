package gs.kar.rblinkist

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapter.Type
import gs.kar.rblinkist.databinding.ItemBlinkBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    class LibraryFragment : Fragment() {

        private val state: State<BlinkistState> by DI.instance()
        private val update: Update<Message, BlinkistState> by DI.instance()

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_library, container, false) as RecyclerView
            view.layoutManager = LinearLayoutManager(context)
            return view
        }

        override fun onStart() {
            super.onStart()
            bind(view as RecyclerView)
        }

        private fun bind(view: RecyclerView) {
            view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            val blinkType = Type<ItemBlinkBinding>(R.layout.item_blink)
                    .onBind {
                        val url = it.binding.item?.volumeInfo?.imageLinks?.default()
                        if (url != null) Glide.with(view).load(url).into(it.binding.thumbnail)
                    }

            launch {
                val observable = ObservableArrayList<Blink>()
                launch(UI) {
                    LastAdapter(observable, BR.item).map<Blink>(blinkType).into(view)
                }
                val channel = state.subscribe()
                channel.consumeEach { blinkist ->
                    launch(UI) { observable.appendFrom(blinkist.blinks) }
                }
            }
        }

    }

}

