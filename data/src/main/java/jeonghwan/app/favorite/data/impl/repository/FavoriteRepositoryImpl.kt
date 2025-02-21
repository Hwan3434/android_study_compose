package jeonghwan.app.favorite.data.impl.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import jeonghwan.app.favorite.data.datasource.FavoriteDatasource
import jeonghwan.app.favorite.datamodel.FavoriteData
import jeonghwan.app.favorite.data.common.toEntity
import jeonghwan.app.favorite.domain.model.FavoriteEntity
import jeonghwan.app.favorite.domain.repository.FavoriteRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
    private val favoriteDatasource: FavoriteDatasource
) : FavoriteRepositoryInterface {
    override fun flowFavorites(): Flow<List<FavoriteEntity>> {
        return favoriteDatasource.getFavorites()
            .map { favoriteDataList ->
                favoriteDataList.map { it.toEntity() }
            }
    }
    override fun getPagedFavorites(): Flow<PagingData<FavoriteEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                favoriteDatasource.getPagedFavorites()
            }
        ).flow.map { pagingData ->
                pagingData.map { it.toEntity() }
            }
    }


    override suspend fun isFavorite(thumbnail: String): Boolean {
        return favoriteDatasource.isThumbnailUrlExists(thumbnail) > 0
    }

    override suspend fun insert(entity: FavoriteEntity) {
        favoriteDatasource.insert(
            FavoriteData(
                dateTime = entity.getDateTime(),
                thumbnail = entity.getThumbnail(),
            )
        )
    }

    override suspend fun remove(thumbnail: String) {
        favoriteDatasource.deleteByThumbnailUrl(thumbnail)
    }
}