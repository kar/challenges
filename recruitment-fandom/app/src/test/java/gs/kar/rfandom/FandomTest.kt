package gs.kar.rfandom

import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class FandomTest {
    @Test
    fun app_willDownloadOnStart() = runBlocking {
        var downloaded = false

        val app = FandomApp(download = { downloaded = true; emptyList() })
        val channel = app.state.subscribe()
        channel.receive()
        assertEquals(true, downloaded)
    }

    @Test
    fun app_willPaginate() = runBlocking {
        var page = 1
        val app = FandomApp(
                apiUrl = "%d %d",
                perPage = 10,
                download = { url ->
                    val index = url.split(" ").last().toInt()
                    assertEquals(page++, index)
                    emptyList()
                }
        )
        val channel = app.state.subscribe()
        channel.receive()
        app.update.send(OnNextPage())
        channel.receive()
        app.update.send(OnNextPage())
        channel.receive()
        app.update.send(OnNextPage())
        channel.receive()
        assertEquals(5, page)
    }

}
