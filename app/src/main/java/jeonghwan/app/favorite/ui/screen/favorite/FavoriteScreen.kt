package jeonghwan.app.favorite.ui.screen.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.domain.model.FavoriteEntity
import jeonghwan.app.favorite.ui.common.ui.LazyPagingGrid
import jeonghwan.app.favorite.ui.common.ui.ThumbnailCard
import kotlinx.coroutines.flow.Flow

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagedFavorites = viewModel.pagedFavorites
    val favoriteSet = viewModel.favoriteFlow

    FavoriteUiScreen(
        state = uiState,
        lazyPagingItems = pagedFavorites,
        favoriteSet = favoriteSet,
        onFavoriteClick = viewModel::toggleFavorite
    )
}


@Composable
fun FavoriteUiScreen(
    state: FavoriteUiState,
    modifier: Modifier = Modifier,
    lazyPagingItems: Flow<PagingData<FavoriteEntity>>,
    favoriteSet: Flow<Set<ContentEntity>>,
    onFavoriteClick: (ContentEntity) -> Unit
) {
    Box(
        modifier = modifier.padding(horizontal = 16.dp),
    ){
        LazyPagingGrid(
            lazyPagingItems = lazyPagingItems,
        ) { item ->
            ThumbnailCard(
                thumbnailUrl = item.getThumbnail(),
                date = item.getDate(),
                time = item.getTime(),
                favoriteSetFlow = favoriteSet,
                onClick = {
                    onFavoriteClick(item)
                }
            )
        }
    }
}


//// Preview for empty state
//@Preview(showBackground = true)
//@Composable
//fun PreviewEmptyLazyPagingGrid() {
//    FavoriteUiScreen(
//        state = FavoriteUiState(),
//        lazyPagingItems = flowOf(PagingData.empty<FavoriteEntity>()).collectAsLazyPagingItems(),
//        favoriteSet = emptySet(),
//        onFavoriteClick = {}
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewPopulatedLazyPagingGrid() {
//
//    val sampleItems = List(5) {
//        FavoriteEntity(
//            thumbnail = "https://example.com/image_$it.jpg",
//            dateTime = 0L,
//        )
//    }
//    FavoriteUiScreen(
//        state = FavoriteUiState(),
//        lazyPagingItems = flowOf(PagingData.from(sampleItems)).collectAsLazyPagingItems(),
//        favoriteSet = setOf(),
//        onFavoriteClick = {}
//    )
//}