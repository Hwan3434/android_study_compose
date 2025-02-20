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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import jeonghwan.app.favorite.domain.model.ContentEntity


@Composable
fun <T : ContentEntity> LazyPagingGrid(
    lazyPagingItems: LazyPagingItems<T>,
    favoriteSet: Set<String>,
    onFavoriteClick: (T) -> Unit
) {
    if (lazyPagingItems.itemCount == 0) {
        EmptyView()
        return
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){

        LazyVerticalStaggeredGrid(
            modifier = Modifier.weight(1f),
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(lazyPagingItems.itemCount) { index ->
                lazyPagingItems[index]?.let { item ->
                    // UI 요소 표시
                    val isFav = favoriteSet.contains(item.getThumbnailUrl())

                    ThumbnailCard(
                        thumbnailUrl = item.getThumbnailUrl(),
                        date = item.getDate(),
                        time = item.getTime(),
                        isFavorite = isFav,
                        onClick = {
                            onFavoriteClick(item)
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