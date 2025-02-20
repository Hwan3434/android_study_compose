package jeonghwan.app.favorite.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import jeonghwan.app.favorite.R
import jeonghwan.app.favorite.ui.screen.favorite.FavoriteScreen
import jeonghwan.app.favorite.ui.screen.search.SearchScreen


sealed class NaviItem(val route: String, private val titleResId: Int, val icon: ImageVector) {
    companion object {
        const val SEARCH = "SEARCH"
        const val FAVORITE = "FAVORITE"
    }

    data object Search : NaviItem(SEARCH, R.string.search_menu, Icons.Filled.Search) {
        @Composable
        override fun GetScreen(
            modifier: Modifier,
        ) {
            SearchScreen(
                modifier = modifier
            )
        }
    }

    data object Favorite : NaviItem(FAVORITE, R.string.favorite_menu, Icons.Filled.Favorite) {
        @Composable
        override fun GetScreen(
            modifier: Modifier,
        ) {
            FavoriteScreen()
        }
    }

    @Composable
    abstract fun GetScreen(
        modifier: Modifier,
    )

    @Composable
    fun GetTab(selected: NaviItem, onClick: () -> Unit) {
        Tab(
            icon = { Icon(this.icon, contentDescription = null) },
            text = { Text(stringResource(this.titleResId)) },
            selected = selected == this,
            onClick = onClick
        )
    }
}

val naviItemsSaver = Saver<NaviItem, String>(
    save = { it.route },
    restore = { route ->
        val tabs = listOf(NaviItem.Search, NaviItem.Favorite)
        tabs.find { it.route == route } ?: NaviItem.Search
    }
)
