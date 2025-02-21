package jeonghwan.app.favorite.core.database.emtity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jeonghwan.app.favorite.core.DBCommon
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = DBCommon.DATABASE_FAVORITE_TABLE)
data class FavoriteData (
    @PrimaryKey(autoGenerate = false)
    val thumbnail: String,
    val dateTime: LocalDateTime,
    val createAt: LocalDateTime = getCurrentLocalDateTime(),
)

private fun getCurrentLocalDateTime(): LocalDateTime {
    return LocalDateTime.now(ZoneId.systemDefault())
}