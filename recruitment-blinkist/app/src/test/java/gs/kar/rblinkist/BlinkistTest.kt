package gs.kar.rblinkist

import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class BlinkistTest {
    @Test
    fun app_willLoadPersistenceOnStart() = runBlocking {
        var loaded = false
        val mockPersistence = object : Persistence {
            override fun load(): List<Blink> {
                loaded = true
                return emptyList()
            }

            override fun save(blinks: List<Blink>) {}
        }

        val app = BlinkistApp(persistence = mockPersistence)
        val channel = app.state.subscribe()
        channel.receive()
        assertEquals(true, loaded)
    }

    @Test
    fun app_willSync() = runBlocking {
        val blink1 = Blink("id_1", BlinkInfo("title 1", null, null))
        val blink2 = Blink("id_2", BlinkInfo("title 2", null, null))
        val blink3 = Blink("id_3", BlinkInfo("title 3", null, null))
        val blink1_new = Blink("id_1", BlinkInfo("title 1", listOf("author 1"), null))

        val mockPersistence = object : Persistence {
            override fun load(): List<Blink> = { listOf(blink1, blink2) }()
            override fun save(blinks: List<Blink>) {}
        }

        val mockApi = object : Api {
            override fun sync(): List<Blink> = { listOf(blink1_new, blink2, blink3) }()
        }

        val app = BlinkistApp(
                persistence = mockPersistence,
                api = mockApi
        )

        val channel = app.state.subscribe()
        var state = channel.receive()
        assertEquals(2, state.blinks.size)
        assertEquals("id_1", state.blinks[0].id)
        assertEquals("id_2", state.blinks[1].id)
        assertEquals(null, state.blinks[0].volumeInfo.authors)

        app.update.send(OnSync())
        state = channel.receive()
        assertEquals(3, state.blinks.size)
        assertEquals("id_3", state.blinks[2].id)
        assertEquals(listOf("author 1"), state.blinks[0].volumeInfo.authors)
    }

}
