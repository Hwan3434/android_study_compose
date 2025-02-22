package jeonghwan.app.favorite.common

import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun nowFLocalDateTime(): LocalDateTime {
    return LocalDateTime.now()
}

fun nowFLong(): Long {
    return nowFLocalDateTime().toFLong()
}

fun Long.yyyyMMdd() : String {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    return this.toFLocalDateTime().format(formatter)
}

fun Long.HHmm() : String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return this.toFLocalDateTime().format(formatter)
}

fun Long.toFLocalDateTime(zone: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return Instant.ofEpochMilli(this).atZone(zone).toLocalDateTime()
}

fun LocalDateTime.toFLong(zone: ZoneId = ZoneId.systemDefault()): Long {
    return this.atZone(zone).toInstant().toEpochMilli()
}

fun String.toFLocalDateTime(): LocalDateTime {
    return OffsetDateTime.parse(this).toLocalDateTime()
}

fun String.toFLong(zone: ZoneId = ZoneId.systemDefault()): Long {
    return this.toFLocalDateTime().toFLong(zone)
}

fun createExpirationLocalDateTime(minute: Long): LocalDateTime {
    return nowFLocalDateTime().plusMinutes(minute)
}
