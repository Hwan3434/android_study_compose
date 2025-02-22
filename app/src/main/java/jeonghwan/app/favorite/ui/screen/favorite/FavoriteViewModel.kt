package jeonghwan.app.favorite.ui.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.domain.model.FavoriteEntity
import jeonghwan.app.favorite.domain.usecase.FavoriteUsecaseInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteUseCase: FavoriteUsecaseInterface,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState: StateFlow<FavoriteUiState> = _uiState
    val pagedFavorites = favoriteUseCase.getPagedFavorites().cachedIn(viewModelScope)
    val favoriteFlow: Flow<Set<FavoriteEntity>> = favoriteUseCase.flowFavorites()
        .map { favorites -> favorites.toSet() }

    fun toggleFavorite(contentEntity: ContentEntity) {
        viewModelScope.launch {
            favoriteUseCase.remove(contentEntity.getThumbnail())
        }
    }
}
