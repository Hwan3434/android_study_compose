package jeonghwan.app.favorite.ui.screen.search

import androidx.compose.foundation.layout.Column
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
import jeonghwan.app.favorite.domain.model.ImageEntity
import jeonghwan.app.favorite.ui.common.ui.LazyPagingGrid
import jeonghwan.app.favorite.ui.common.ui.SearchBar
import jeonghwan.app.favorite.ui.common.ui.ThumbnailCard
import jeonghwan.app.favorite.ui.common.ui.containsThumbnailUrl
import kotlinx.coroutines.flow.flowOf


@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val lazyPagingItems = viewModel.pagingData.collectAsLazyPagingItems()
    val favoriteSet by viewModel.favoriteFlow.collectAsState(initial = emptySet())

    SearchUiScreen(
        state = uiState,
        lazyPagingItems = lazyPagingItems,
        onQueryChange = { viewModel.updateQuery(it) },
        favoriteSet = favoriteSet,
        onFavoriteClick = {
            viewModel.toggleFavorite(it)
        }
    )
}

@Composable
fun SearchUiScreen(
    modifier: Modifier = Modifier,
    state: SearchUiState,
    lazyPagingItems: LazyPagingItems<ContentEntity>,
    favoriteSet: Set<ContentEntity>,
    onQueryChange: (String) -> Unit,
    onFavoriteClick: (ContentEntity) -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        SearchBar(
            text = state.query,
        ) {
            onQueryChange(it)
        }

        LazyPagingGrid(
            lazyPagingItems = lazyPagingItems,
        ) { item ->
            // UI 요소 표시
            val isFav = favoriteSet.containsThumbnailUrl(item)

            ThumbnailCard(
                thumbnailUrl = item.getThumbnail(),
                date = item.getDate(),
                time = item.getTime(),
                isFavorite = isFav,
                onClick = {
                    onFavoriteClick(item)
                }
            )
        }
    }
}


// Preview for empty state
@Preview(showBackground = true)
@Composable
fun PreviewEmptyLazyPagingGrid() {
    SearchUiScreen(
        state = SearchUiState(),
        lazyPagingItems = flowOf(PagingData.empty<ContentEntity>()).collectAsLazyPagingItems(),
        favoriteSet = emptySet(),
        onFavoriteClick = {},
        onQueryChange = { }
    )
}

// Preview for populated state
@Preview(showBackground = true)
@Composable
fun PreviewPopulatedLazyPagingGrid() {

    val sampleItems = List<ContentEntity>(5) {
        ImageEntity(
            thumbnail = "https://example.com/image_$it.jpg",
            displaySiteName = "site $it",
            imageUrl = "https://example",
            width = 100,
            height = 100,
            docUrl = "https://example.com/doc_$it",
            collection = "collection $it",
            dateTime = 0L,
        )
    }
    SearchUiScreen(
        state = SearchUiState(
            query = "query"
        ),
        lazyPagingItems = flowOf(PagingData.from(sampleItems)).collectAsLazyPagingItems(),
        favoriteSet = setOf(),
        onFavoriteClick = {},
        onQueryChange = { }
    )
}