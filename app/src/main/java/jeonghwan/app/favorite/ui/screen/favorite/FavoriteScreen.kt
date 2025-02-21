package jeonghwan.app.favorite.ui.screen.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.domain.model.FavoriteEntity
import jeonghwan.app.favorite.ui.common.ui.LazyPagingGrid
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber
import java.time.LocalDateTime

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
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<FavoriteEntity>,
    favoriteSet: Set<FavoriteEntity>,
    onFavoriteClick: (ContentEntity) -> Unit
) {
    Timber.d("FavoriteUiScreen: ${lazyPagingItems.itemCount}")
    Box(
        modifier = modifier.padding(horizontal = 16.dp),
    ){
        LazyPagingGrid(
            lazyPagingItems = lazyPagingItems,
            selectedThumbnailUrl = favoriteSet,
            onClick = onFavoriteClick
        )
    }
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

@Preview(showBackground = true)
@Composable
fun PreviewPopulatedLazyPagingGrid() {

    val sampleItems = List(5) {
        FavoriteEntity(
            thumbnail = "https://example.com/image_$it.jpg",
            dateTime = LocalDateTime.now(),
        )
    }
    FavoriteUiScreen(
        lazyPagingItems = flowOf(PagingData.from(sampleItems)).collectAsLazyPagingItems(),
        favoriteSet = setOf(),
        onFavoriteClick = {}
    )
}