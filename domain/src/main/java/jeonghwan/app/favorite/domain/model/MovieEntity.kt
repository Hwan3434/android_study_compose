package jeonghwan.app.favorite.domain.model

import kotlinx.datetime.LocalDateTime

/**
 * Movie Repository에서 반환하는 데이터
 */
data class MovieEntity(
    val title: String,
    val playTime: Int,
    val thumbnail: String,
    val url: String,
    override val dateTime: LocalDateTime,
    val author: String
) : ContentEntity(
    dateTime = dateTime
) {
    override fun getThumbnailUrl(): String {
        return thumbnail
    }
}