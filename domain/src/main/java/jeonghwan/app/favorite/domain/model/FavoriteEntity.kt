package jeonghwan.app.favorite.domain.model

import java.time.LocalDateTime

data class FavoriteEntity (
    val thumbnail: String,
    val dateTime: LocalDateTime,
) : ContentEntity(
    localDateTime = dateTime
) {
    override fun getThumbnailUrl(): String {
        return thumbnail
    }
}
