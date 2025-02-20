package jeonghwan.app.favorite.ui.screen.favorite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.domain.model.FavoriteEntity
import jeonghwan.app.favorite.ui.common.ui.LazyPagingGrid
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDateTime

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val pagedFavorites = viewModel.pagedFavorites.collectAsLazyPagingItems()
    val favoriteSet by viewModel.favoriteFlow.collectAsState(initial = emptySet())

    FavoriteUiScreen(
        lazyPagingItems = pagedFavorites, // State의 값을 전달합니다.
        favoriteSet = favoriteSet,
        onFavoriteClick = viewModel::toggleFavorite
    )
}

@Composable
fun FavoriteUiScreen(
    lazyPagingItems: LazyPagingItems<FavoriteEntity>,
    favoriteSet: Set<String>,
    onFavoriteClick: (ContentEntity) -> Unit
) {
    LazyPagingGrid(
        lazyPagingItems = lazyPagingItems,
        selectedThumbnailUrl = favoriteSet,
        onClick = onFavoriteClick
    )
}


// Preview for empty state
@Preview(showBackground = true)
@Composable
fun PreviewEmptyLazyPagingGrid() {
    FavoriteUiScreen(
        lazyPagingItems = flowOf(PagingData.empty<FavoriteEntity>()).collectAsLazyPagingItems(),
        favoriteSet = emptySet(),
        onFavoriteClick = {}
    )
}

// Preview for populated state
@Preview(showBackground = true)
@Composable
fun PreviewPopulatedLazyPagingGrid() {

    val sampleItems = List(5) {
        FavoriteEntity(
            thumbnail = "https://example.com/image_$it.jpg",
            dateTime = LocalDateTime(2021, 8, 1, 12, 34, 56),
        )
    }
    FavoriteUiScreen(
        lazyPagingItems = flowOf(PagingData.from(sampleItems)).collectAsLazyPagingItems(),
        favoriteSet = setOf(sampleItems[0].getThumbnailUrl()),
        onFavoriteClick = {}
    )
}