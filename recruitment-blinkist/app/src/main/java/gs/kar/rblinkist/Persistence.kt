package gs.kar.rblinkist

import io.paperdb.Paper

interface Persistence {
    fun load(): List<Blink>
    fun save(blinks: List<Blink>)
}

class PaperPersistence: Persistence {
    override fun load(): List<Blink> {
        return Paper.book().read("blinks", emptyList())
    }

    override fun save(blinks: List<Blink>) {
        Paper.book().write("blinks", blinks)
    }
}
