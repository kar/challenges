package gs.kar.r3pi

import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

/**
 * R3pi.kt provides models and business logic specific to this app.
 *
 * Overview:
 * - Volume*, R3piState: The models,
 * - Message: The concrete messages emitted in the app,
 * - R3piApp: The business logic,
 * - DI: A Dependency Injection module.
 *
 * The R3piApp class exposes several parameters while providing their default implementations,
 * which makes the business logic easily testable without the need for using a mocking framework.
 */

data class Volumes(val items: List<Volume>) {
    class Deserializer : ResponseDeserializable<Volumes> {
        override fun deserialize(content: String) = Gson().fromJson(content, Volumes::class.java)
    }
}

data class Volume(
        val id: String,
        val volumeInfo: VolumeInfo
)

data class VolumeInfo(
        val title: String,
        val authors: List<String>?,
        val description: String?,
        val imageLinks: VolumeImages?
) {
    fun allAuthors(): String {
        return authors?.joinToString(", ") ?: ""
    }
}

data class VolumeImages(
        val smallThumbnail: String?,
        val thumbnail: String?,
        val small: String?,
        val medium: String?
) {
    fun lowQuality(): String {
        return thumbnail ?: smallThumbnail!!
    }

    fun highQuality(): String {
        return medium ?: small ?: thumbnail!!
    }
}


data class R3piState(
        val volumes: List<Volume> = emptyList(),
        val page: Int = 0,
        val detailVolume: Volume? = null
)

sealed class Message
class OnInit: Message()
class OnNextPage: Message()
class OnBack: Message()
data class OnTapped(val volume: Volume): Message()

class R3piApp(
        val search: String = "android",
        val apiKey: String = "AIzaSyDPgncRnPs0O6I17HkzUM-_nihYiHugHco",
        val perPage: Int = 40,
        val apiUrl: String = "https://www.googleapis.com/books/v1/volumes?q=%s&key=%s&maxResults=%d&startIndex=%d",
        val initState: R3piState = R3piState(),
        val download: (String) -> List<Volume> = { url ->
            val (_, _, result) = url.httpGet().responseObject(deserializer = Volumes.Deserializer())
            result.fold(success = { it.items },
            failure = { emptyList() })
        }
) {

    val state = State(initState)
    val update = Update(state, ::handler)

    init {
        update.send(OnInit())
    }

    private fun handler(msg: Message, state: R3piState): R3piState {
        return when (msg) {
            is OnInit -> {
                val url = apiUrl.format(search, apiKey, perPage, 0)
                val volumes = download(url)
                state.copy(volumes = volumes)
            }
            is OnNextPage -> {
                val startIndex = (state.page + 1) * perPage
                val url = apiUrl.format(search, apiKey, perPage, startIndex)
                val volumes = download(url)
                state.copy(volumes = state.volumes + volumes, page = state.page + 1)
            }
            is OnTapped -> {
                state.copy(detailVolume = msg.volume)
            }
            is OnBack -> {
                state.copy(detailVolume = null)
            }
        }
    }
}

val DI = Kodein {
    val app = R3piApp()
    bind<State<R3piState>>() with singleton { app.state }
    bind<Update<Message, R3piState>>() with singleton { app.update }
}

