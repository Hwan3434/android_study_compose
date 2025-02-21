package jeonghwan.app.favorite.domain.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class FavoriteEntity (
    private val thumbnail: String,
    private val dateTime: Long,
) : ContentEntity {
    override fun getThumbnail(): String {
        return thumbnail
    }

    override fun getDateTime(): Long {
        return dateTime
    }

    override fun getDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        return super.convertLongToLocalDateTime(dateTime).format(formatter)
    }

    override fun getTime(): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return super.convertLongToLocalDateTime(dateTime).format(formatter)
    }
}
