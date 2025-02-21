package jeonghwan.app.favorite.domain.model

import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * GridView에서 보여질 데이터
 */
abstract class ContentEntity(
    val localDateTime: LocalDateTime,
) {
    abstract fun getThumbnailUrl(): String

    fun getDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        return localDateTime.format(formatter)
    }

    fun getTime(): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return localDateTime.format(formatter)
    }

}
