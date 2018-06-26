package gs.kar.r3pi

import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class R3piTest {
    @Test
    fun app_willDownloadOnStart() = runBlocking {
        var downloaded = false

        val app = R3piApp(download = { downloaded = true; emptyList() })
        val channel = app.state.subscribe()
        channel.receive()
        assertEquals(true, downloaded)
    }

    @Test
    fun app_willPaginate() = runBlocking {
        var page = 0
        val app = R3piApp(
                apiUrl = "%s %s %d %d",
                perPage = 10,
                download = { url ->
                    val index = url.split(" ").last().toInt()
                    assertEquals(page++ * 10, index)
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
        assertEquals(4, page)
    }

    @Test
    fun app_willNavigate() = runBlocking {
        val book = Volume("id", VolumeInfo("title", null, null, null))

        val app = R3piApp(download = { listOf(book) })
        val channel = app.state.subscribe()

        var state = channel.receive()
        assertEquals(null, state.detailVolume)

        app.update.send(OnTapped(book))
        state = channel.receive()
        assertEquals(book, state.detailVolume)

        app.update.send(OnBack())
        state = channel.receive()
        assertEquals(null, state.detailVolume)
    }
}
