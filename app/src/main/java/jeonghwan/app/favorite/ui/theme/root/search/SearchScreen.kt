package jeonghwan.app.favorite.ui.theme.root.search

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    SearchUiScreen()
}

@Composable
fun SearchUiScreen() {
    Text("SearchUiScreen")
}