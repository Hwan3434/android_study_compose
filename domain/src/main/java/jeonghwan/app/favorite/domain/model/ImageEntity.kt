package jeonghwan.app.favorite.domain.model

import jeonghwan.app.favorite.common.HHmm
import jeonghwan.app.favorite.common.yyyyMMdd


/**
 * Image Repository에서 반환하는 데이터
 */
data class ImageEntity(
    val collection: String,
    private val thumbnail: String,
    val imageUrl: String,
    val width: Int,
    val height: Int,
    val displaySiteName: String,
    val docUrl: String,
    private val dateTime: Long,
) : ContentEntity{
    override fun getThumbnail(): String {
        return thumbnail
    }

    override fun getDateTime(): Long {
        return dateTime
    }

    override fun getDate(): String {
        return dateTime.yyyyMMdd()
    }

    override fun getTime(): String {
        return dateTime.HHmm()
    }
}
