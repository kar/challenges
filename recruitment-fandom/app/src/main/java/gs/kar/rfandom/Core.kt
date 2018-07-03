package gs.kar.rfandom

import kotlinx.coroutines.experimental.channels.*
import kotlinx.coroutines.experimental.launch

/**
 * Core.kt provides a basic implementation of the Elm architecture reusable in other projects.
 *
 * Overview:
 * - State (S): Contains the explicit state of the app and propagates state changes,
 * - Message (M): Emitted as a result of user action,
 * - Update: Handles Messages and provides new State.
 *
 * State A -> Update(Message, State A) -> State B
 *
 * State is immutable and implementation is using Kotlin coroutines in order to provide concurrency
 * and take it off the main thread.
 */

class State<S>(init: S) {

    internal var container = init
        set(value) {
            field = value
            launch {
                broadcast.send(value)
            }
        }

    private val broadcast = BroadcastChannel<S>(Channel.CONFLATED)

    fun subscribe(): ReceiveChannel<S> {
        return broadcast.openSubscription()
    }

}

class Update<M, S>(
        state: State<S>,
        update: (M, S) -> S
) {

    private val channel = Channel<M>(Channel.CONFLATED)

    init {
        launch {
            channel.consumeEach { msg ->
                state.container = update(msg, state.container)
            }
        }
    }

    fun send(msg: M) = launch {
        channel.send(msg)
    }
}
