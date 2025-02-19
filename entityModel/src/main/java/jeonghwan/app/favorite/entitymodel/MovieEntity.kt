package jeonghwan.app.favorite.entitymodel

import kotlinx.datetime.LocalDateTime

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