package jeonghwan.app.favorite.domain.model

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


/**
 * GridView에서 보여질 데이터
 */
interface ContentEntity {
    fun getThumbnail(): String
    fun getDateTime(): Long
    fun getDate(): String
    fun getTime(): String
    fun convertLongToLocalDateTime(timestamp: Long): LocalDateTime {
        return try {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("UTC"))
        } catch (e: Exception) {
            // 변환에 실패하면 오늘 날짜를 반환합니다.
            LocalDateTime.now()
        }
    }

}
