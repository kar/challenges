package gs.kar.rblinkist

import com.github.kittinunf.fuel.httpGet

interface Api {
    fun sync(): List<Blink>
}

internal class MockApi(
        val apiKey: String = "AIzaSyDPgncRnPs0O6I17HkzUM-_nihYiHugHco",
        val perPage: Int = 40,
        val pages: Int = 5,
        val apiUrl: String = "https://www.googleapis.com/books/v1/volumes?q=communication&key=%s&maxResults=%d&startIndex=%d"
): Api {
    override fun sync(): List<Blink> {
        var blinks = emptyList<Blink>()
        repeat(pages) { page ->
            val (_, _, result) = apiUrl.format(apiKey, perPage, perPage * page).httpGet()
                    .responseObject(deserializer = Blinks.Deserializer())
            blinks += result.fold(success = { it.items },
                    failure = { emptyList() })
        }
        return blinks
    }
}

