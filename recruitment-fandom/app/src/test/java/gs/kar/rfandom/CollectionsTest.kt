package gs.kar.rfandom

import android.databinding.ObservableArrayList
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CollectionsTest {
    @Test
    fun appendFrom_willAppendOnlyNewItems() = runBlocking {
        val observable = ObservableArrayList<Int>()
        val items = listOf(1, 2, 3)

        observable.appendFrom(items)
        assertEquals(3, observable.size)

        val items2 = listOf(0, 0, 0, 4, 5)
        observable.appendFrom(items2)
        assertEquals(5, observable.size)
        assertEquals(1, observable[0])
        assertEquals(5, observable[4])
    }

}
