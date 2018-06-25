package gs.kar.abihome

import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

/**
 * Abihome.kt provides models and business logic specific to this app.
 *
 * Overview:
 * - Image, AbihomeState: The models,
 * - Message: The concrete messages emitted in the app,
 * - AbihomeApp: The business logic,
 * - DI: A Dependency Injection module.
 *
 * The AbihomeApp class exposes several parameters while providing their default implementations,
 * which makes the business logic easily testable without the need for using a mocking framework.
 */

data class Image(val url: String)

data class AbihomeState(
        val images: List<Image> = emptyList(),
        val tappedImages: List<Image> = emptyList()
)

sealed class Message
data class OnInit(val imagesUrl: String): Message()
data class OnSelectionChange(val newImage: Image): Message()

class AbihomeApp(
        val imagesUrl: String = "http://weyveed.herokuapp.com/test/images",
        val initState: AbihomeState = AbihomeState(),
        val download: (String) -> List<Image> = {
            val (_, _, result) = imagesUrl.httpGet().responseJson()
            result.fold(success = {
                // JsonArray makes this a bit ugly
                val array = it.array()
                var images = emptyList<Image>()
                var i = 0
                while (i < array.length()) {
                    images += Image(array.getString(i++))
                }
                images
            },
            failure = { emptyList() })
        }
) {

    val state = State(initState)
    val update = Update(state, ::handler)

    init {
        update.send(OnInit(imagesUrl))
    }

    private fun handler(msg: Message, state: AbihomeState): AbihomeState {
        return when (msg) {
            is OnInit -> {
                val images = download(msg.imagesUrl)
                state.copy(images = images)
            }
            is OnSelectionChange -> {
                val images = listOf(msg.newImage) + when (state.tappedImages.size) {
                    0 -> emptyList()
                    1 -> listOf(state.tappedImages[0])
                    else -> state.tappedImages.subList(0, 1)
                }
                state.copy(tappedImages = images)
            }
        }
    }
}

val DI = Kodein {
    val app = AbihomeApp()
    bind<State<AbihomeState>>() with singleton { app.state }
    bind<Update<Message, AbihomeState>>() with singleton { app.update }
}

