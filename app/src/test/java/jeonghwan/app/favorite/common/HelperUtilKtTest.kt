package jeonghwan.app.favorite.common

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class HelperUtilKtTest {

    @Test
    fun testNowFunctions() {
        val nowDateTime: LocalDateTime = nowFLocalDateTime()
        val nowEpoch: Long = nowFLong()
        val convertedDateTime: LocalDateTime = nowEpoch.toFLocalDateTime()

        // 허용 오차 1,000ms (1초)
        val diffMillis = kotlin.math.abs(nowDateTime.toFLong() - nowEpoch)
        assertTrue("nowLocalDateTime와 nowLong 사이의 차이가 1,000ms 미만이어야 합니다.", diffMillis < 1000)

        val diffMillis2 = kotlin.math.abs(convertedDateTime.toFLong() - nowEpoch)
        assertTrue("toLocalDateTime 확장 함수가 올바르게 작동해야 합니다.", diffMillis2 < 1000)
    }

    @Test
    fun testLongExtensionsAndFormatting() {
        val fixedDateTime: LocalDateTime = LocalDateTime.of(2025, 2, 22, 15, 0, 0)
        val epochMillis: Long = fixedDateTime.toFLong()
        val convertedDateTime: LocalDateTime = epochMillis.toFLocalDateTime()

        // LocalDateTime과 Long 간의 상호 변환이 정확해야 합니다.
        assertEquals(fixedDateTime, convertedDateTime)

        // 포맷 함수 검증: yyyyMMdd와 HHmm
        val formattedDate: String = epochMillis.yyyyMMdd()   // 예상: "2025.02.22"
        val formattedTime: String = epochMillis.HHmm()        // 예상: "15:00"

        assertEquals("2025.02.22", formattedDate)
        assertEquals("15:00", formattedTime)
    }

    @Test
    fun testStringConversion() {
        val dateString = "2017-06-21T15:59:30.000+09:00"

        // String -> LocalDateTime 변환
        val localDateTimeFromString: LocalDateTime = dateString.toFLocalDateTime()

        // String -> Long 변환 후 다시 LocalDateTime으로 변환
        val epochMillis: Long = dateString.toFLong()
        val localDateTimeFromLong: LocalDateTime = epochMillis.toFLocalDateTime()

        // 두 LocalDateTime이 동일한지 확인합니다.
        assertEquals(localDateTimeFromString, localDateTimeFromLong)
    }

    @Test
    fun testCreateExpirationLocalDateTime() {
        val currentTime: LocalDateTime = nowFLocalDateTime()
        val expirationTime: LocalDateTime = createExpirationLocalDateTime(1)

        // 예상 시간 차이는 60,000ms (1분)이며, 허용 오차는 1,000ms
        val diffMillis = expirationTime.toFLong() - currentTime.toFLong()
        val expectedDiff = 60000L

        assertTrue(
            "createExpirationLocalDateTime은 1분(60,000ms) 후의 시간을 반환해야 합니다.",
            kotlin.math.abs(diffMillis - expectedDiff) < 1000
        )
    }

    @Test
    fun testRepeatedConversionCycle() {
        // 초기 고정 날짜: 2025-02-22 15:00:00
        val originalDateTime: LocalDateTime = LocalDateTime.of(2025, 2, 22, 15, 0, 0)
        var currentLong: Long = originalDateTime.toFLong()

        // 10회 반복: Long -> LocalDateTime -> toString(ISO offset 형식) -> LocalDateTime -> Long
        repeat(10) { _ ->
            // Long을 LocalDateTime으로 변환
            val dt = currentLong.toFLocalDateTime()
            // LocalDateTime을 ISO_OFFSET_DATE_TIME 형식 문자열로 변환
            val offsetString = dt.atZone(java.time.ZoneId.systemDefault())
                .format(java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            // 문자열을 다시 LocalDateTime으로 변환 (우리의 확장 함수는 Offset 형식의 문자열 파싱을 기반으로 함)
            val dtFromString = offsetString.toFLocalDateTime()
            // LocalDateTime을 Long으로 변환하여 다음 사이클에 사용
            currentLong = dtFromString.toFLong()
        }
        val finalDateTime: LocalDateTime = currentLong.toFLocalDateTime()

        // 여러 번의 사이클 후에도 초기 날짜와 동일해야 합니다.
        assertEquals(originalDateTime, finalDateTime)
    }
}