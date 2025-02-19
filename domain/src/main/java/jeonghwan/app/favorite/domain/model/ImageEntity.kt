package jeonghwan.app.favorite.domain.model

import kotlinx.datetime.LocalDateTime

data class ImageEntity(
    val collection: String,
    val thumbnail: String,
    val imageUrl: String,
    val width: Int,
    val height: Int,
    val displaySiteName: String,
    val docUrl: String,
    override val dateTime: LocalDateTime
) : ContentEntity(
    dateTime = dateTime
) {
    override fun getThumbnailUrl(): String {
        return thumbnail
    }
}
