package gs.kar.abihome

import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test

import org.junit.Assert.*

class ExampleUnitTest {
    @Test
    fun app_willDownloadOnStart() = runBlocking {
        var downloadCalled = false

        val app = AbiApp(download = {
            downloadCalled = true
            emptyList()
        })

        app.state.subscribe().receive()
        assertEquals(true, downloadCalled)
    }

    @Test
    fun app_willBroadcastImages() = runBlocking {
        val images = listOf(Image("example.png"))

        val app = AbiApp(download = { images })
        val state = app.state.subscribe()
        assertEquals(images, state.receive().images)
    }

    @Test
    fun app_willUpdateSelection() = runBlocking {
        val images = listOf(Image("example.png"), Image("example2.png"))
        val (img1, img2) = images

        val app = AbiApp(download = { images })
        val state = app.state.subscribe()
        state.receive()

        app.update.send(OnSelectionChange(img1))
        var abi = state.receive()
        assertEquals(img1, abi.selectedImage)
        assertEquals(null, abi.previousImage)

        app.update.send(OnSelectionChange(img2))
        abi = state.receive()
        assertEquals(img2, abi.selectedImage)
        assertEquals(img1, abi.previousImage)
    }

}
