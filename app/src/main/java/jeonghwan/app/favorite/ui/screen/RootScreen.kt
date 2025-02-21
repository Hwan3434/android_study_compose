package jeonghwan.app.favorite.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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

private val NAVI_TABS = listOf(Navigation.Search, Navigation.Favorite)
private val naviItemsSaver = Saver<Navigation, String>(
    save = { it.route },
    restore = { route ->
        NAVI_TABS.find { it.route == route } ?: Navigation.Search
    }
)

@Composable
fun RootScreen(
    modifier: Modifier = Modifier,
) {
    var selectedTab by rememberSaveable(stateSaver = naviItemsSaver) { mutableStateOf(Navigation.Search) }
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
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            saveableStateHolder.SaveableStateProvider(key = selectedTab.route) {
                selectedTab.GetScreen(modifier)
            }
        }
    }
}


@Composable
fun BottomBar(
    currentItem: Navigation,
    onChanged: (Navigation) -> Unit,
){
    TabRow(
        selectedTabIndex = NAVI_TABS.indexOf(currentItem),
        tabs = {
            NAVI_TABS.forEach { tab ->
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

    var selectedTab by rememberSaveable(stateSaver = naviItemsSaver) { mutableStateOf(Navigation.Search) }
    val tabs = listOf(Navigation.Search, Navigation.Favorite)

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