package gs.kar.rfandom

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

data class Wikis(val items: List<Wiki>) {
    class Deserializer : ResponseDeserializable<Wikis> {
        override fun deserialize(content: String) = Gson().fromJson(content, Wikis::class.java)
    }
}

data class Wiki(
        val id: String,
        val title: String,
        val stats: WikiStats,
        val image: String?
)

data class WikiStats(
        val articles: Int
) {
    fun articlesString(): String {
        return "Articles: ${articles / 1000}k"
    }
}

data class FandomState(
        val wikis: List<Wiki> = emptyList(),
        val page: Int = 1
)

sealed class Message
class OnInit: Message()
class OnNextPage: Message()

class FandomApp(
        val perPage: Int = 25,
        val apiUrl: String = "http://www.wikia.com/api/v1/Wikis/List?expand=1&limit=%d&batch=%d",
        val initState: FandomState = FandomState(),
        val download: (String) -> List<Wiki> = { url ->
            val (_, _, result) = url.httpGet().responseObject(deserializer = Wikis.Deserializer())
            result.fold(success = { it.items },
            failure = { emptyList() })
        }
) {

    val state = State(initState)
    val update = Update(state, ::handler)

    init {
        update.send(OnInit())
    }

    private fun handler(msg: Message, state: FandomState): FandomState {
        return when (msg) {
            is OnInit -> {
                val url = apiUrl.format(perPage, 1)
                val wikis = download(url)
                state.copy(wikis = wikis)
            }
            is OnNextPage -> {
                val startIndex = (state.page + 1) * perPage
                val url = apiUrl.format(perPage, startIndex)
                val wikis = download(url)
                state.copy(wikis = state.wikis + wikis, page = state.page + 1)
            }
        }
    }
}

val DI = Kodein {
    val app = FandomApp()
    bind<State<FandomState>>() with singleton { app.state }
    bind<Update<Message, FandomState>>() with singleton { app.update }
}

