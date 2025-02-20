package jeonghwan.app.favorite.domain.repository

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import jeonghwan.app.favorite.domain.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteDao {
    @Insert
    suspend fun insert(user: FavoriteEntity)

    @Query("SELECT * FROM ${AppDatabase.DATABASE_FAVORITE_TABLE} ORDER BY createAt ASC")
    fun getPagedFavorites(): PagingSource<Int, FavoriteEntity>

    @Query("SELECT * FROM ${AppDatabase.DATABASE_FAVORITE_TABLE}")
    fun getFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT COUNT(*) FROM ${AppDatabase.DATABASE_FAVORITE_TABLE} WHERE thumbnail = :thumbnail")
    suspend fun isThumbnailUrlExists(thumbnail: String): Int

    suspend fun delete(entity: FavoriteEntity) {
        deleteByThumbnailUrl(entity.thumbnail)
    }

    @Query("DELETE FROM ${AppDatabase.DATABASE_FAVORITE_TABLE} WHERE thumbnail = :thumbnail")
    suspend fun deleteByThumbnailUrl(thumbnail: String)
}