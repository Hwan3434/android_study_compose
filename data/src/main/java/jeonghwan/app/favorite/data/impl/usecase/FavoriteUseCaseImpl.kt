package jeonghwan.app.favorite.data.impl.usecase

import androidx.paging.PagingData
import jeonghwan.app.favorite.domain.model.FavoriteEntity
import jeonghwan.app.favorite.domain.repository.FavoriteRepositoryInterface
import jeonghwan.app.favorite.domain.usecase.FavoriteUsecaseInterface
import kotlinx.coroutines.flow.Flow

class FavoriteUseCaseImpl (
    private val favoriteRepositoryInterface: FavoriteRepositoryInterface
) : FavoriteUsecaseInterface {
    override fun flowFavorites(): Flow<List<FavoriteEntity>> {
        return favoriteRepositoryInterface.flowFavorites()
    }

    override fun getPagedFavorites(): Flow<PagingData<FavoriteEntity>> {
        return favoriteRepositoryInterface.getPagedFavorites()
    }

    override suspend fun insert(entity: FavoriteEntity) {
        favoriteRepositoryInterface.insert(entity)
    }

    override suspend fun remove(thumbnail: String) {
        favoriteRepositoryInterface.remove(thumbnail)
    }

    override suspend fun isFavorite(thumbnail: String): Boolean {
        return favoriteRepositoryInterface.isFavorite(thumbnail)
    }
}