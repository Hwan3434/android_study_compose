package jeonghwan.app.favorite.domain.repository

import androidx.paging.PagingData
import jeonghwan.app.favorite.domain.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow


interface FavoriteRepositoryInterface {
    fun flowFavorites(): Flow<List<FavoriteEntity>>
    fun getPagedFavorites(): Flow<PagingData<FavoriteEntity>>
    suspend fun isFavorite(thumbnail: String): Boolean
    suspend fun insert(entity: FavoriteEntity)
    suspend fun remove(thumbnail: String)
}