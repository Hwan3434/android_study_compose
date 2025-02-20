package jeonghwan.app.favorite.ui.screen.favorite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.domain.model.FavoriteEntity
import jeonghwan.app.favorite.ui.common.ui.LazyPagingGrid

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val pagedFavorites = viewModel.pagedFavorites.collectAsLazyPagingItems()
    val favoriteSet by viewModel.favoriteFlow.collectAsState(initial = emptySet())

    FavoriteUiScreen(
        lazyPagingItems = pagedFavorites, // State의 값을 전달합니다.
        favoriteSet = favoriteSet,
        onClick = viewModel::toggleFavorite
    )
}

@Composable
fun FavoriteUiScreen(
    lazyPagingItems: LazyPagingItems<FavoriteEntity>,
    favoriteSet: Set<String>,
    onClick: (ContentEntity) -> Unit
) {
    LazyPagingGrid(
        lazyPagingItems = lazyPagingItems,
        favoriteSet = favoriteSet,
        onFavoriteClick = onClick
    )
}