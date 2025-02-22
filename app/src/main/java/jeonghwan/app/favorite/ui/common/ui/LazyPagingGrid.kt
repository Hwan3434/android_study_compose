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
import kotlinx.coroutines.flow.flowOf


// 즐겨찾기 판단 여부
fun <T : ContentEntity> Set<T>.containsThumbnailUrl(target: ContentEntity): Boolean {
    return this.any { it.getThumbnail() == target.getThumbnail() }
}

@Composable
fun <T : ContentEntity> LazyPagingGrid(
    lazyPagingItems: LazyPagingItems<T>,
    compose: @Composable (T) -> Unit
) {
    if (lazyPagingItems.itemCount == 0) {
        EmptyView()
        return
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val touchModifier = Modifier.pointerInput(Unit) {
        detectTapGestures {
            keyboardController?.hide()
        }
    }
    val scrollState = rememberLazyStaggeredGridState()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y != 0f) {
                    keyboardController?.hide()
                }
                return Offset.Zero
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(touchModifier)
            .nestedScroll(nestedScrollConnection)
    ){

        LazyVerticalStaggeredGrid(
            state = scrollState,
            modifier = Modifier.weight(1f),
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(lazyPagingItems.itemCount) { index ->
                lazyPagingItems[index]?.let { item ->
                    compose(item)
                }
            }
        }

        lazyPagingItems.apply {
            when (loadState.append) {
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
                    if (loadState.append.endOfPaginationReached) {
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
        lazyPagingItems = flowOf(PagingData.empty<ImageEntity>()).collectAsLazyPagingItems(),
    ) {
        ThumbnailCard(
            thumbnailUrl = "abc",
            date = nowFLong().yyyyMMdd(),
            time = nowFLong().HHmm(),
            isFavorite = false,
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
        lazyPagingItems = flowOf(PagingData.from(sampleItems)).collectAsLazyPagingItems(),
    ) {
        ThumbnailCard(
            thumbnailUrl = "abc",
            date = nowFLong().yyyyMMdd(),
            time = nowFLong().HHmm(),
            isFavorite = false,
            onClick = {}
        )
    }
}