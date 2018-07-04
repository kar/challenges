package gs.kar.rfandom

import android.databinding.ObservableArrayList

fun <T> ObservableArrayList<T>.appendFrom(list: List<T>) {
    val lastIndex = size - 1
    if (lastIndex < 0) addAll(list)
    else  {
        val newIndex = lastIndex + 1
        if (newIndex < list.size) addAll(list.subList(newIndex, list.size))
    }
}
