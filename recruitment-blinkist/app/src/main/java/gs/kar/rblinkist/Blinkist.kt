package gs.kar.rblinkist

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton


/**
 * Blinkist.kt provides business logic specific to this app.
 *
 * Overview:
 * - Message: The concrete messages emitted in the app,
 * - BlinkistApp: The business logic,
 * - DI: A Dependency Injection module.
 *
 * The BlinkistApp class exposes several parameters while providing their default implementations,
 * which makes the business logic easily testable without the need for using a mocking framework.
 */

data class BlinkistState(
        val blinks: List<Blink> = emptyList()
)

sealed class Message
class OnInit: Message()
class OnSync: Message()

class BlinkistApp(
        val initState: BlinkistState = BlinkistState(),
        val api: Api = MockApi(),
        val persistence: Persistence = PaperPersistence()
) {

    val state = State(initState)
    val update = Update(state, ::handler)

    fun init() {
        update.send(OnInit())
    }

    private fun handler(msg: Message, state: BlinkistState): BlinkistState {
        return when (msg) {
            is OnInit -> {
                val blinks = persistence.load()
                state.copy(blinks = blinks)
            }
            is OnSync -> {
                val serverBlinks = api.sync()
                // Just to make things more interesting, lets assume we are supposed to only update
                // some metadata about blinks already known locally, but not the entire objects.
                val oldBlinks = state.blinks
                val newBlinks = serverBlinks.map { new ->
                    val old = oldBlinks.find { new == it }
                    if (old != null) Blink(
                            id = old.id,
                            volumeInfo = new.volumeInfo
                    ) else new
                }
                state.copy(blinks = newBlinks)
            }
        }
    }
}

val DI = Kodein {
    val app = BlinkistApp()
    bind<State<BlinkistState>>() with singleton { app.state }
    bind<Update<Message, BlinkistState>>() with singleton { app.update }
}

