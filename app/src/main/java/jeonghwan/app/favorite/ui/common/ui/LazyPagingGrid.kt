package jeonghwan.app.favorite.ui.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import jeonghwan.app.favorite.R
import jeonghwan.app.favorite.common.HHmm
import jeonghwan.app.favorite.common.nowFLong
import jeonghwan.app.favorite.common.yyyyMMdd
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.domain.model.ImageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber

@Composable
fun <T : ContentEntity> LazyPagingGrid(
    lazyPagingItems: () -> Flow<PagingData<T>>,
    compose: @Composable (T) -> Unit
) {
    val items = lazyPagingItems().collectAsLazyPagingItems()
    val appendLoadState by remember {
        derivedStateOf { items.loadState.append }
    }
    Column {
        LazyVerticalStaggeredGrid(
            modifier = Modifier.weight(1f),
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(
                count = items.itemCount,
                key = { index ->
                    items[index]?.getThumbnail() ?: index
                }
            ) { index ->
                val item = items[index]
                if (item != null) {
                    compose(item)
                }
            }
        }
        Footer(
            appendLoadState = appendLoadState
        )
    }
}

@Composable
private fun Footer(appendLoadState: LoadState) {
    when (appendLoadState) {
        is LoadState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.data_error),
                    color = Color.Red
                )
            }
        }

        is LoadState.NotLoading -> {
            if (appendLoadState.endOfPaginationReached) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    stringResource(R.string.all_data_fetched_message),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
private fun EmptyView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "No Data",
                modifier = Modifier.size(64.dp)
            )
            Text(stringResource(R.string.data_not_found), modifier = Modifier.padding(top = 8.dp))
        }
    }
}

// Preview for empty state
@Preview(showBackground = true)
@Composable
fun PreviewEmptyLazyPagingGrid() {
    LazyPagingGrid(
        lazyPagingItems = { flowOf(PagingData.empty<ImageEntity>()) },
    ) {
        ThumbnailCard(
            thumbnailUrl = "abc",
            date = nowFLong().yyyyMMdd(),
            time = nowFLong().HHmm(),
            favoriteSetFlow = { flowOf() },
            onClick = {}
        )
    }
}

// Preview for populated state
@Preview(showBackground = true)
@Composable
fun PreviewPopulatedLazyPagingGrid() {
    val sampleItems = List(5) {
        ImageEntity(
            displaySiteName = "site $it",
            imageUrl = "https://example.com/image_$it.jpg",
            thumbnail = "https://example.com/image_$it.jpg",
            width = 100,
            height = 100,
            docUrl = "https://example.com/doc_$it",
            collection = "collection $it",
            dateTime = 0L,
        )
    }
    LazyPagingGrid(
        lazyPagingItems = { flowOf(PagingData.from(sampleItems)) },
    ) {
        ThumbnailCard(
            thumbnailUrl = "abc",
            date = nowFLong().yyyyMMdd(),
            time = nowFLong().HHmm(),
            favoriteSetFlow = { flowOf() },
            onClick = {}
        )
    }
}