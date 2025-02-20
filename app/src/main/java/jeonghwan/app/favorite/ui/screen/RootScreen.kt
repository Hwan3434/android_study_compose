package jeonghwan.app.favorite.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jeonghwan.app.favorite.ui.screen.search.SearchScreen


private val naviItemsSaver = Saver<NaviItem, String>(
    save = { it.route },
    restore = { route ->
        val tabs = listOf(NaviItem.Search, NaviItem.Favorite)
        tabs.find { it.route == route } ?: NaviItem.Search
    }
)

@Composable
fun RootScreen(
    modifier: Modifier = Modifier,
) {
    var selectedTab by rememberSaveable(stateSaver = naviItemsSaver) { mutableStateOf(NaviItem.Search) }
    val saveableStateHolder = rememberSaveableStateHolder()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomBar(
                currentItem = selectedTab
            ) {
                selectedTab = it
            }
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


@Composable
fun BottomBar(
    currentItem: NaviItem,
    onChanged: (NaviItem) -> Unit,
){
    val tabs = listOf(NaviItem.Search, NaviItem.Favorite)
    TabRow(
        selectedTabIndex = tabs.indexOf(currentItem),
        tabs = {
            tabs.forEach { tab ->
                tab.GetTab(
                    currentItem,
                ) {
                    onChanged(tab)
                }
            }
        }
    )
}

@Preview
@Composable
fun RootScreenPreview() {

    var selectedTab by rememberSaveable(stateSaver = naviItemsSaver) { mutableStateOf(NaviItem.Search) }
    val tabs = listOf(NaviItem.Search, NaviItem.Favorite)

    Scaffold(
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
        Box(modifier = Modifier.padding(innerPadding)) {
            Text("메인화면")
        }
    }
}