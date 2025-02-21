package jeonghwan.app.favorite.core.database.datasource

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import jeonghwan.app.favorite.core.DBCommon
import jeonghwan.app.favorite.core.database.emtity.FavoriteData
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteDatasource {
    @Insert
    suspend fun insert(user: FavoriteData)

    @Query("SELECT * FROM ${DBCommon.DATABASE_FAVORITE_TABLE} ORDER BY createAt ASC")
    fun getPagedFavorites(): PagingSource<Int, FavoriteData>

    @Query("SELECT * FROM ${DBCommon.DATABASE_FAVORITE_TABLE}")
    fun getFavorites(): Flow<List<FavoriteData>>

    @Query("SELECT COUNT(*) FROM ${DBCommon.DATABASE_FAVORITE_TABLE} WHERE thumbnail = :thumbnail")
    suspend fun isThumbnailUrlExists(thumbnail: String): Int

    @Query("DELETE FROM ${DBCommon.DATABASE_FAVORITE_TABLE} WHERE thumbnail = :thumbnail")
    suspend fun deleteByThumbnailUrl(thumbnail: String)
}