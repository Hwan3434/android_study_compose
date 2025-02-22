package jeonghwan.app.favorite.domain.model

import jeonghwan.app.favorite.common.HHmm
import jeonghwan.app.favorite.common.yyyyMMdd


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
    override fun getThumbnail(): String = thumbnail
    override fun getDateTime(): Long = dateTime
    override fun getDate(): String = dateTime.yyyyMMdd()
    override fun getTime(): String = dateTime.HHmm()
}