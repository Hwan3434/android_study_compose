package jeonghwan.app.favorite.domain.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * Movie Repository에서 반환하는 데이터
 */
data class MovieEntity(
    val title: String,
    val playTime: Int,
    private val thumbnail: String,
    val url: String,
    val author: String,
    private val dateTime: Long,
) : ContentEntity{
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