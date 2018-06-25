package gs.kar.abihome

import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test

import org.junit.Assert.*

class AbihomeTest {
    @Test
    fun app_willDownloadOnStart() = runBlocking {
        var downloadCalled = false

        val app = AbihomeApp(download = {
            downloadCalled = true
            emptyList()
        })

        app.state.subscribe().receive()
        assertEquals(true, downloadCalled)
    }

    @Test
    fun app_willBroadcastImages() = runBlocking {
        val images = listOf(Image("example.png"))

        val app = AbihomeApp(download = { images })
        val state = app.state.subscribe()
        assertEquals(images, state.receive().images)
    }

    @Test
    fun app_willUpdateSelection() = runBlocking {
        val images = listOf(Image("example.png"), Image("example2.png"))
        val (img1, img2) = images

        val app = AbihomeApp(download = { images })
        val state = app.state.subscribe()
        state.receive()

        app.update.send(OnSelectionChange(img1))
        var abi = state.receive()
        assertEquals(img1, abi.tappedImages[0])
        assertEquals(null, abi.tappedImages.getOrNull(1))

        app.update.send(OnSelectionChange(img2))
        abi = state.receive()
        assertEquals(img2, abi.tappedImages[0])
        assertEquals(img1, abi.tappedImages[1])
    }

}
