package jeonghwan.app.favorite.ui.theme.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun RootScreen(
    modifier: Modifier = Modifier,
) {
    var selectedTab by rememberSaveable(stateSaver = naviItemsSaver) { mutableStateOf(NaviItem.Search) }
    val tabs = listOf(NaviItem.Search, NaviItem.Favorite)

    val saveableStateHolder = rememberSaveableStateHolder()

    Scaffold(
        modifier = modifier
            .safeContentPadding(),
        bottomBar = {
            TabRow(
                selectedTabIndex = tabs.indexOf(selectedTab),
                tabs = {
                    tabs.forEach { tab ->
                        tab.GetTab(
                            selectedTab,
                        ) {
                            selectedTab = tab
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            saveableStateHolder.SaveableStateProvider(key = selectedTab.route) {
                selectedTab.GetScreen(
                    modifier,
                )
            }
        }
    }

}

@Preview
@Composable
fun RootScreenPreview() {
    RootScreen()
}