package jeonghwan.app.favorite.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.domain.model.FavoriteEntity
import jeonghwan.app.favorite.domain.usecase.ContentUseCaseInterface
import jeonghwan.app.favorite.domain.usecase.FavoriteUsecaseInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val contentUseCase: ContentUseCaseInterface,
    private val favoriteUseCase: FavoriteUsecaseInterface
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState


    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val pagingData: Flow<PagingData<ContentEntity>> = _uiState
        .map { it.query }
        .debounce(500)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(PagingData.empty())
            } else {
                Pager(
                    config = PagingConfig(
                        initialLoadSize = 30,
                        pageSize = 10,
                        enablePlaceholders = false
                    ),
                    pagingSourceFactory = {
                        ContentPagingSource(
                            contentUseCase,
                            query,
                        )
                    }
                ).flow
            }
        }
        .cachedIn(viewModelScope)

    // 즐겨찾기 상태를 수집
    val favoriteFlow: Flow<Set<FavoriteEntity>> = favoriteUseCase.flowFavorites()
        .map { favorites -> favorites.toSet() }

    // 사용자 이벤트 처리를 위한 함수 예시
    fun updateQuery(newQuery: String) {
        _uiState.value = _uiState.value.copy(query = newQuery)
    }

    fun toggleFavorite(contentEntity: ContentEntity) {
        viewModelScope.launch {
            if (favoriteUseCase.isFavorite(contentEntity.getThumbnail())) {
                favoriteUseCase.remove(contentEntity.getThumbnail())
            } else {
                favoriteUseCase.insert(
                    FavoriteEntity(
                        thumbnail = contentEntity.getThumbnail(),
                        dateTime = contentEntity.getDateTime()
                    )
                )
            }
        }
    }
}
