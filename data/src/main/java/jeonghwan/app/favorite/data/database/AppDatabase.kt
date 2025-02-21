package jeonghwan.app.favorite.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import jeonghwan.app.favorite.data.datasource.CacheDatasource
import jeonghwan.app.favorite.data.datasource.FavoriteDatasource
import jeonghwan.app.favorite.datamodel.CacheData
import jeonghwan.app.favorite.datamodel.FavoriteData
import jeonghwan.app.favorite.datamodel.ImageDocumentModel
import jeonghwan.app.favorite.datamodel.KakaoModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


@Database(entities = [FavoriteData::class, CacheData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDatasource(): FavoriteDatasource
    abstract fun cacheDatasource(): CacheDatasource
}


class Converters {
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): Long? {
        val result = dateTime?.atZone(ZoneId.of("UTC"))?.toInstant()?.toEpochMilli()
        return result
    }

    @TypeConverter
    fun toLocalDateTime(dateTimeLong: Long?): LocalDateTime? {
        val result = dateTimeLong?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.of("UTC"))
        }
        return result
    }

    @TypeConverter
    fun fromKakaoModel(value: KakaoModel<ImageDocumentModel>?): String? {
        return if (value == null) null else Gson().toJson(value)
    }

    @TypeConverter
    fun toKakaoModel(value: String?): KakaoModel<ImageDocumentModel>? {
        return if (value == null) null else Gson().fromJson(value, object : TypeToken<KakaoModel<ImageDocumentModel>>() {}.type)
    }
}

