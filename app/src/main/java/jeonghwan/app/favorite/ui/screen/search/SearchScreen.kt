package jeonghwan.app.favorite.ui.screen.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.ui.common.ui.LazyPagingGrid
import jeonghwan.app.favorite.ui.common.ui.SearchBar


@Composable
fun SearchScreen(
    modifier: Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val lazyPagingItems = viewModel.pagingData.collectAsLazyPagingItems()
    val favoriteSet by viewModel.favoriteFlow.collectAsState(initial = emptySet())

    SearchUiScreen(
        modifier = modifier.padding(16.dp),
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
    modifier: Modifier,
    lazyPagingItems: LazyPagingItems<ContentEntity>,
    favoriteSet: Set<String>,
    onQueryChange: (String) -> Unit,
    onFavoriteClick: (ContentEntity) -> Unit = {},
) {
    Column(
        modifier = modifier,
    ) {
        SearchBar {
            onQueryChange(it)
        }

        LazyPagingGrid(
            lazyPagingItems = lazyPagingItems,
            favoriteSet = favoriteSet,
            onFavoriteClick = onFavoriteClick
        )

//        LazyVerticalStaggeredGrid(
//            columns = StaggeredGridCells.Fixed(2), // 세 개의 열로 고정
//            verticalItemSpacing = 4.dp,
//            horizontalArrangement = Arrangement.spacedBy(4.dp),
//        ) {
//            items(count = lazyPagingItems.itemCount) { index ->
//                lazyPagingItems[index]?.let { item ->
//                    val isFav = favoriteSet.contains(item.getThumbnailUrl())
//
//                    ThumbnailCard(
//                        thumbnailUrl = item.getThumbnailUrl(),
//                        date = item.getDate(),
//                        time = item.getTime(),
//                        isFavorite = isFav,
//                        onClick = {
//                            onFavoriteClick(item)
//                        }
//                    )
//                }
//            }
//
//            // 추가 로딩 상태에 따른 UI 처리
//            lazyPagingItems.apply {
//                when (loadState.append) {
//                    is androidx.paging.LoadState.Loading -> {
//                        item {
//                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
//                        }
//                    }
//
//                    is androidx.paging.LoadState.Error -> {
//                        item {
//                            Text("Error loading more items", modifier = Modifier.padding(16.dp))
//                        }
//                    }
//
//                    else -> {}
//                }
//            }
//        }
    }
}

