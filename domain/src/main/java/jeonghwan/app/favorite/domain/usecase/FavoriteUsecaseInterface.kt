package jeonghwan.app.favorite.domain.usecase

import androidx.paging.PagingData
import jeonghwan.app.favorite.domain.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteUsecaseInterface {
    fun flowFavorites(): Flow<List<FavoriteEntity>>
    fun getPagedFavorites(): Flow<PagingData<FavoriteEntity>>
    suspend fun insert(entity: FavoriteEntity)
    suspend fun remove(thumbnail: String)
    suspend fun isFavorite(thumbnail: String): Boolean
}