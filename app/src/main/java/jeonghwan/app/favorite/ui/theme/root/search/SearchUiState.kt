package jeonghwan.app.favorite.ui.theme.root.search

import jeonghwan.app.favorite.domain.model.ContentEntity

data class SearchUiState(
    val query: String = "",
    val results: List<ContentEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
