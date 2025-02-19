package jeonghwan.app.favorite.ui.screen.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.ui.common.ui.SearchBar
import timber.log.Timber


@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by searchViewModel.uiState.collectAsState()
    val lazyPagingItems = searchViewModel.pagingData.collectAsLazyPagingItems()

    SearchUiScreen(
        query = uiState.query,
        lazyPagingItems = lazyPagingItems,
        onQueryChange = { searchViewModel.updateQuery(it) },
    )
}

@Composable
fun SearchUiScreen(
    query: String,
    lazyPagingItems: LazyPagingItems<ContentEntity>,
    onQueryChange: (String) -> Unit,
) {
    Timber.d("lazyPagingItems.itemCount :: ${lazyPagingItems.itemCount}")
    Column {
        SearchBar {
            Timber.d("SearchBar: $it")
            onQueryChange(it)
        }

        // 검색 결과 리스트 (PagingData 사용)
        LazyColumn {
            items(count = lazyPagingItems.itemCount) { index ->
                lazyPagingItems[index]?.let { item ->
                    // ContentEntity를 렌더링하는 Composable (예: Text)

                    AsyncImage(
                        modifier = Modifier,
                        model = item.getThumbnailUrl(),
                        contentDescription = "thumbnail",
                    )
                }
            }

            // 추가 로딩 상태에 따른 UI 처리
            lazyPagingItems.apply {
                when (loadState.append) {
                    is androidx.paging.LoadState.Loading -> {
                        item {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        }
                    }
                    is androidx.paging.LoadState.Error -> {
                        item {
                            Text("Error loading more items", modifier = Modifier.padding(16.dp))
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}

