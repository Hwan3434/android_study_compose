package jeonghwan.app.favorite.ui.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import jeonghwan.app.favorite.R

@Composable
fun ThumbnailCard(
    thumbnailUrl: String,
    date: String,
    time: String,
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }, // 클릭 이벤트 설정
        shape = RoundedCornerShape(16.dp), // 테두리 라운딩
        elevation = CardDefaults.cardElevation(4.dp), // 엘리베이션
        colors = CardDefaults.cardColors(containerColor = Color.White) // 카드 배경색
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize() // Card의 전체 영역을 차지하도록 설정
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(), // Card의 전체 영역을 차지하도록 설정
                model = ImageRequest.Builder(LocalContext.current)
                    .data(thumbnailUrl) // 로드할 이미지 URL
                    .crossfade(true) // 이미지가 로드되면서 서서히 보여지는 효과 적용
                    .placeholder(R.drawable.baseline_downloading_24) // 로딩 중 표시할 이미지
                    .error(R.drawable.baseline_error_24) // 로딩 실패 시 표시할 이미지
                    .build(),
                contentScale = ContentScale.Crop, // 이미지를 Crop 해서 보여줌
                contentDescription = "thumbnail",
            )

            if (isFavorite) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "favorite",
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.TopEnd)
                        .padding(4.dp),
                    tint = Color.Yellow
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(
                        color = Color.LightGray.copy(alpha = 0.8f), // 알파 50% 적용
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Text(date, fontWeight = FontWeight.Bold)
                Text(time, fontWeight = FontWeight.Bold)
            }
        }

    }
}

@Preview(showBackground = true, widthDp = 340, heightDp = 340)
@Composable
fun ThumbnailCardPreview() {
    ThumbnailCard(
        thumbnailUrl = "https://example.com/image.jpg",
        date = "2025-02-20",
        time = "12:00 PM",
        isFavorite = false,
        onClick = { /* 클릭 시 동작 */ }
    )
}