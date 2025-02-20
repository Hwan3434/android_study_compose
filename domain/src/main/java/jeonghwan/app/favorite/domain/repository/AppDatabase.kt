package jeonghwan.app.favorite.domain.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import jeonghwan.app.favorite.domain.model.FavoriteEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


@Database(entities = [FavoriteEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        const val DATABASE_NAME = "favorite.db"
        const val DATABASE_FAVORITE_TABLE = "favorite"
    }
}


class Converters {
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): Long? {
        return dateTime?.toInstant(TimeZone.UTC)?.toEpochMilliseconds()
    }

    @TypeConverter
    fun toLocalDateTime(dateTimeLong: Long?): LocalDateTime? {
        return dateTimeLong?.let {
            Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC)
        }
    }
}

