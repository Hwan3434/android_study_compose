package jeonghwan.app.favorite.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import jeonghwan.app.favorite.common.toFLocalDateTime
import jeonghwan.app.favorite.common.toFLong
import jeonghwan.app.favorite.data.datasource.CacheDatasource
import jeonghwan.app.favorite.data.datasource.FavoriteDatasource
import jeonghwan.app.favorite.datamodel.CacheData
import jeonghwan.app.favorite.datamodel.FavoriteData
import java.time.LocalDateTime


@Database(entities = [FavoriteData::class, CacheData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDatasource(): FavoriteDatasource
    abstract fun cacheDatasource(): CacheDatasource
}

class Converters {
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): Long? {
        return dateTime?.toFLong()
    }

    @TypeConverter
    fun toLocalDateTime(dateTimeLong: Long?): LocalDateTime? {
        return dateTimeLong?.toFLocalDateTime()
    }
}

