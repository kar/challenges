package gs.kar.rblinkist

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class Blinks(val items: List<Blink>) {
    class Deserializer : ResponseDeserializable<Blinks> {
        override fun deserialize(content: String) = Gson().fromJson(content, Blinks::class.java)
    }
}

data class Blink(
        val id: String,
        val volumeInfo: BlinkInfo
) {
    override fun equals(other: Any?): Boolean {
        return if (other is Blink) id.equals(other.id)
        else false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

data class BlinkInfo(
        val title: String,
        val authors: List<String>?,
        val imageLinks: BlinkImages?
) {
    fun allAuthors(): String {
        return authors?.joinToString(", ") ?: ""
    }
}

data class BlinkImages(
        val smallThumbnail: String?,
        val thumbnail: String?,
        val small: String?
) {
    fun default(): String {
        return small ?: thumbnail ?: smallThumbnail!!
    }
}
