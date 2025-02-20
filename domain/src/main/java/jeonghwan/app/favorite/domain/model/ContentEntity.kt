package jeonghwan.app.favorite.domain.model

import kotlinx.datetime.LocalDateTime

/**
 * GridView에서 보여질 데이터
 */
abstract class ContentEntity(
    open val dateTime: LocalDateTime,
) {
    abstract fun getThumbnailUrl(): String

    fun getDate(): String {
        val year = dateTime.year
        val month = dateTime.monthNumber.toString().padStart(2, '0')
        val day = dateTime.dayOfMonth.toString().padStart(2, '0')
        return "$year.$month.$day"
    }

    fun getTime(): String {
        val hour = dateTime.hour.toString().padStart(2, '0')
        val minute = dateTime.minute.toString().padStart(2, '0')
        return "$hour:$minute"
    }

}