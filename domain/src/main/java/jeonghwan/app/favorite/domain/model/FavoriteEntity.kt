package jeonghwan.app.favorite.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import jeonghwan.app.favorite.domain.repository.AppDatabase
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Entity(tableName = AppDatabase.DATABASE_FAVORITE_TABLE)
data class FavoriteEntity (
    @PrimaryKey(autoGenerate = false)
    val thumbnail: String,
    val createAt: LocalDateTime = getCurrentLocalDateTime(),
    override val dateTime: LocalDateTime,
) : ContentEntity(
    dateTime = dateTime
) {
    override fun getThumbnailUrl(): String {
        return thumbnail
    }
}


private fun getCurrentLocalDateTime(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}
