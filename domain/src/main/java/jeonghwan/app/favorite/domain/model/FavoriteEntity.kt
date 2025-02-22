package jeonghwan.app.favorite.domain.model

import jeonghwan.app.favorite.common.HHmm
import jeonghwan.app.favorite.common.yyyyMMdd

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
        return dateTime.yyyyMMdd()
    }

    override fun getTime(): String {
        return dateTime.HHmm()
    }
}
