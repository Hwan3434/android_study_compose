package jeonghwan.app.favorite.ui.theme.root.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jeonghwan.app.favorite.domain.model.QueryEntity
import jeonghwan.app.favorite.domain.model.QuerySort
import jeonghwan.app.favorite.domain.usecase.ContentUseCaseInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val contentUseCase: ContentUseCaseInterface
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    // 사용자 이벤트 처리를 위한 함수 예시
    fun updateQuery(newQuery: String) {
        _uiState.value = _uiState.value.copy(query = newQuery)
    }

    fun performSearch() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = contentUseCase.getContent(QueryEntity(query = _uiState.value.query, sort = QuerySort.ACCURACY, page = 1, size = 10))
            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(results = result.getOrDefault(emptyList()), isLoading = false)
            } else {
                _uiState.value.copy(isLoading = false, errorMessage = result.exceptionOrNull()?.message)
            }
        }
    }
}
