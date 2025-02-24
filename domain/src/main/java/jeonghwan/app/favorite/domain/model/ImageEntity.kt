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
    override fun getThumbnail(): String = thumbnail
    override fun getDateTime(): Long = dateTime
    override fun getDate(): String = dateTime.yyyyMMdd()
    override fun getTime(): String = dateTime.HHmm()
    override fun equals(other: Any?): Boolean {
        if (other !is ContentEntity) return false
        return thumbnail == other.getThumbnail() &&
                getDate() == other.getDate() &&
                getTime() == other.getTime()
    }

    override fun hashCode(): Int {
        var result = thumbnail.hashCode()
        result = 31 * result + getDate().hashCode()
        result = 31 * result + getTime().hashCode()
        return result
    }
}
