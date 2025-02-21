package jeonghwan.app.favorite.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import jeonghwan.app.favorite.core.DBCommon
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = DBCommon.DATABASE_FAVORITE_TABLE)
data class FavoriteData (
    @PrimaryKey(autoGenerate = false)
    val thumbnail: String,
    val dateTime: Long,
    val createAt: Long = getCurrentLocalDateTime(),
)

private fun getCurrentLocalDateTime(): Long {
    return convertLocalDateTimeToLong(LocalDateTime.now(ZoneId.systemDefault()))
}

fun convertLocalDateTimeToLong(dateTime: LocalDateTime): Long {
    return try {
        dateTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli()
    } catch (e: Exception) {
        // 변환에 실패하면 현재 시각을 타임스탬프로 변환하여 반환합니다.
        LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().toEpochMilli()
    }
}