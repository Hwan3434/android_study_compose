package jeonghwan.app.favorite.ui.screen.favorite


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.domain.model.FavoriteEntity
import jeonghwan.app.favorite.domain.repository.FavoriteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val dao: FavoriteDao
) : ViewModel() {

    val pagedFavorites: Flow<PagingData<FavoriteEntity>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = { dao.getPagedFavorites() }
    ).flow
        .cachedIn(viewModelScope)

    val favoriteFlow: Flow<Set<String>> = dao.getFavorites()
        .map { favorites -> favorites.map { it.getThumbnailUrl() }.toSet() }

    fun toggleFavorite(contentEntity: ContentEntity) {
        viewModelScope.launch {
            if (isFavorite(contentEntity)) {
                dao.deleteByThumbnailUrl(contentEntity.getThumbnailUrl())
            } else {
                dao.insert(
                    FavoriteEntity(
                        thumbnail = contentEntity.getThumbnailUrl(),
                        dateTime = contentEntity.dateTime,
                    )
                )
            }
        }
    }

    private suspend fun isFavorite(contentEntity: ContentEntity): Boolean {
        return dao.isThumbnailUrlExists(contentEntity.getThumbnailUrl()) > 0
    }
}
