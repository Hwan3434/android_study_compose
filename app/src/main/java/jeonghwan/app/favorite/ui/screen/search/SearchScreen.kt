package jeonghwan.app.favorite.ui.screen.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.domain.model.ImageEntity
import jeonghwan.app.favorite.ui.common.ui.SearchBar
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import timber.log.Timber


@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by searchViewModel.uiState.collectAsState()

    SearchUiScreen(
        query = uiState.query,
        results = uiState.results,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        onQueryChange = { searchViewModel.updateQuery(it) },
        onSearch = { searchViewModel.performSearch() }
    )
}

@Composable
fun SearchUiScreen(
    query: String,
    results: List<ContentEntity>,
    isLoading: Boolean,
    errorMessage: String?,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    // 예시 UI 구성
    Column(modifier = Modifier.padding(16.dp)) {
        // 검색어 입력 및 검색 버튼 등
        // TextField, Button 등을 배치
        // 예시:
        SearchBar(
            hint = "아서라"
        ) {
            Timber.d("입력값 : $it")
        }
        if (isLoading) {
            Text("Loading...", color = Color.Gray)
        }
        errorMessage?.let {
            Text("Error: $it", color = Color.Red)
        }
        // 결과 표시
        results.forEach { result ->
            Text(text = result.toString())
        }
    }
}

@Preview
@Composable
fun PreviewSearchScreen() {
    SearchUiScreen(
        query = "Hello",
        results = listOf(
            ImageEntity(
                collection = "Collection",
                thumbnail = "Thumbnail",
                imageUrl = "ImageUrl",
                width = 100,
                height = 100,
                displaySiteName = "SiteName",
                docUrl = "DocUrl",
                dateTime = LocalDateTime(2025, Month.FEBRUARY, 17, 10, 0, 0)
            )
        ),
        isLoading = false,
        errorMessage = null,
        onQueryChange = {},
        onSearch = {}
    )
}