package gs.kar.abihome

import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import kotlinx.coroutines.experimental.channels.*
import kotlinx.coroutines.experimental.launch
import java.util.*

// Reusable components
sealed class Message

class State<T>(init: T) {

    internal var container = init
        set(value) {
            field = value
            launch {
                broadcast.send(value)
            }
        }

    private val broadcast = BroadcastChannel<T>(Channel.CONFLATED)

    fun subscribe(): ReceiveChannel<T> {
        return broadcast.openSubscription()
    }

}

class Update<T>(
        state: State<T>,
        update: (Message, T) -> T
) {

    internal val channel = Channel<Message>(Channel.CONFLATED)

    init {
        launch {
            channel.consumeEach { msg ->
                state.container = update(msg, state.container)
            }
        }
    }

    fun send(msg: Message) = launch {
        channel.send(msg)
    }
}

// Components specific to this project
data class Image(val url: String)

data class Abi(
        val images: List<Image> = emptyList(),
        val selectedImage: Image? = null,
        val previousImage: Image? = null
)

data class OnInit(val imagesUrl: String): Message()
data class OnSelectionChange(val newImage: Image): Message()

class AbiApp(
        val imagesUrl: String = "http://weyveed.herokuapp.com/test/images",
        val state: State<Abi> = State(Abi()),
        val download: (String) -> List<Image> = {
            val (_, _, result) = imagesUrl.httpGet().responseJson()
            result.fold(success = {
                val array = it.array()
                var images = emptyList<Image>()
                var i = 0
                while (i < array.length()) {
                    images.plus(Image(array.getString(i)))
                }
                images
            },
            failure = { emptyList() })
        }
) {

    val update = Update(state, ::update)

    init {
        update.send(OnInit(imagesUrl))
    }

    private fun update(msg: Message, state: Abi): Abi {
        return when (msg) {
            is OnInit -> {
                val images = download(msg.imagesUrl)
                state.copy(images = images)
            }
            is OnSelectionChange -> {
                val previousImage = state.selectedImage
                state.copy(selectedImage = msg.newImage, previousImage = previousImage)
            }
        }
    }
}


