package jeonghwan.app.favorite.core.database.emtity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jeonghwan.app.favorite.core.DBCommon
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = DBCommon.DATABASE_CACHE_TABLE)
data class CacheData (
    @PrimaryKey val key: String,
    val jsonData: String,
    val expirationTime: LocalDateTime = getExpirationLocalDateTime(),
)

private fun getExpirationLocalDateTime(): LocalDateTime {
    return LocalDateTime.now(ZoneId.systemDefault()).plusMinutes(3)
}
