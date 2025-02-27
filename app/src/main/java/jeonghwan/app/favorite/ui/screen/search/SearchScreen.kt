package jeonghwan.app.favorite.ui.screen.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.ui.common.ui.LazyPagingGrid
import jeonghwan.app.favorite.ui.common.ui.SearchBar
import jeonghwan.app.favorite.ui.common.ui.ThumbnailCard
import kotlinx.coroutines.flow.Flow


@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    SearchUiScreen(
        state = uiState.query,
        lazyPagingItems = viewModel::pagingData,
        onQueryChange = viewModel::updateQuery,
        favoriteSet = viewModel::favoriteFlow,
        onFavoriteClick = viewModel::toggleFavorite
    )
}

@Composable
fun SearchUiScreen(
    state: String,
    lazyPagingItems: () -> Flow<PagingData<ContentEntity>>,
    favoriteSet: () -> Flow<Set<ContentEntity>>,
    onQueryChange: (String) -> Unit,
    onFavoriteClick: (ContentEntity) -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        SearchBar(
            text = state,
        ) {
            onQueryChange(it)
        }

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
                },
            )

        }
    }
}


// Preview for empty state
//@Preview(showBackground = true)
//@Composable
//fun PreviewEmptyLazyPagingGrid() {
//    SearchUiScreen(
//        state = SearchUiState(),
//        lazyPagingItems = flowOf(PagingData.empty<ContentEntity>()).collectAsLazyPagingItems(),
//        favoriteSet = { false },
//        onFavoriteClick = {},
//        onQueryChange = { }
//    )
//}
//
//// Preview for populated state
//@Preview(showBackground = true)
//@Composable
//fun PreviewPopulatedLazyPagingGrid() {
//
//    val sampleItems = List<ContentEntity>(5) {
//        ImageEntity(
//            thumbnail = "https://example.com/image_$it.jpg",
//            displaySiteName = "site $it",
//            imageUrl = "https://example",
//            width = 100,
//            height = 100,
//            docUrl = "https://example.com/doc_$it",
//            collection = "collection $it",
//            dateTime = 0L,
//        )
//    }
//    SearchUiScreen(
//        state = SearchUiState(
//            query = "query"
//        ),
//        lazyPagingItems = flowOf(PagingData.from(sampleItems)).collectAsLazyPagingItems(),
//        favoriteSet = { false },
//        onFavoriteClick = {},
//        onQueryChange = { }
//    )
//}