package jeonghwan.app.favorite.domain.model

import java.time.LocalDateTime


/**
 * Image Repository에서 반환하는 데이터
 */
data class ImageEntity(
    val collection: String,
    val thumbnail: String,
    val imageUrl: String,
    val width: Int,
    val height: Int,
    val displaySiteName: String,
    val docUrl: String,
    val dateTime: LocalDateTime,
) : ContentEntity(
    localDateTime = dateTime
) {
    override fun getThumbnailUrl(): String {
        return thumbnail
    }
}
