package jeonghwan.app.favorite.domain.model

import java.time.LocalDateTime


/**
 * Movie Repository에서 반환하는 데이터
 */
data class MovieEntity(
    val title: String,
    val playTime: Int,
    val thumbnail: String,
    val url: String,
    val author: String,
    val dateTime: LocalDateTime,
) : ContentEntity(
    localDateTime = dateTime
) {
    override fun getThumbnailUrl(): String {
        return thumbnail
    }
}