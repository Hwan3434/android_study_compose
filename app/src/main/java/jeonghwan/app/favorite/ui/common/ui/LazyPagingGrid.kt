package jeonghwan.app.favorite.ui.common.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.domain.model.ImageEntity
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDateTime


@Composable
fun <T : ContentEntity> LazyPagingGrid(
    lazyPagingItems: LazyPagingItems<T>,
    selectedThumbnailUrl: Set<String>,
    onClick: (T) -> Unit
) {
    if (lazyPagingItems.itemCount == 0) {
        EmptyView()
        return
    }

    val keyboardController = LocalSoftwareKeyboardController.current

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
                    // UI 요소 표시
                    val isFav = selectedThumbnailUrl.contains(item.getThumbnailUrl())

                    ThumbnailCard(
                        thumbnailUrl = item.getThumbnailUrl(),
                        date = item.getDate(),
                        time = item.getTime(),
                        isFavorite = isFav,
                        onClick = {
                            onClick(item)
                            keyboardController?.hide()
                        }
                    )
                }
            }
        }

        lazyPagingItems.apply {
            when (loadState.append) {
                is androidx.paging.LoadState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }

                is androidx.paging.LoadState.Error -> {
                    Text("Error loading more items", modifier = Modifier.padding(16.dp))
                }

                is androidx.paging.LoadState.NotLoading -> {
                    if (loadState.append.endOfPaginationReached) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "모든 데이터를 조회했습니다.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(8.dp)
                                ) // 둥근 모서리 적용
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold, // 볼드체
                            fontSize = 18.sp, // 글자 크기 조정
                            color = Color.DarkGray // 텍스트 색상
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
            Text("데이터가 없습니다.", modifier = Modifier.padding(top = 8.dp))
        }
    }
}

// Preview for empty state
@Preview(showBackground = true)
@Composable
fun PreviewEmptyLazyPagingGrid() {
    LazyPagingGrid(
        lazyPagingItems = flowOf(PagingData.empty<ImageEntity>()).collectAsLazyPagingItems(),
        selectedThumbnailUrl = emptySet(),
        onClick = {}
    )
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
            dateTime = LocalDateTime(2021, 8, 1, 12, 34, 56),
        )
    }
    LazyPagingGrid(
        lazyPagingItems = flowOf(PagingData.from(sampleItems)).collectAsLazyPagingItems(),
        selectedThumbnailUrl = setOf(sampleItems[0].getThumbnailUrl()),
        onClick = {}
    )
}