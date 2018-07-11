package gs.kar.rblinkist

import android.app.Application
import io.paperdb.Paper
import org.kodein.di.generic.instance

class MainApplication : Application() {

    private val update: Update<Message, BlinkistState> by DI.instance()

    override fun onCreate() {
        super.onCreate()
        Paper.init(this)
        update.send(OnInit())
        update.send(OnSync())
    }
}
