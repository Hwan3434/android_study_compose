package jeonghwan.app.favorite.domain.model

/**
 * GridView에서 보여질 데이터
 */
interface ContentEntity {
    fun getThumbnail(): String
    fun getDateTime(): Long
    fun getDate(): String
    fun getTime(): String
}
