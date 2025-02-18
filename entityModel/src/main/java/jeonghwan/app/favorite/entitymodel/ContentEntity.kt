package jeonghwan.app.favorite.entitymodel

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

interface ContentEntity {
    fun getThumbnailUrl(): String
    fun getDateTime(): Pair<String, String>

    fun formatDateTimeHigh(dateTime: String): Pair<String, String> {
        // ISO 8601 형식의 문자열을 OffsetDateTime으로 파싱 (예: "2017-06-21T15:59:30.000+09:00")
        val odt = OffsetDateTime.parse(dateTime)

        // 날짜와 시간 포맷터 정의
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        // 날짜와 시간 문자열 추출
        val formattedDate = odt.toLocalDate().format(dateFormatter)
        val formattedTime = odt.toLocalTime().format(timeFormatter)

        return formattedDate to formattedTime
    }
}