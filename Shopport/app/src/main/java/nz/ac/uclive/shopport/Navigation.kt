package nz.ac.uclive.shopport

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import nz.ac.uclive.shopport.explore.ExploreScreen
import nz.ac.uclive.shopport.giftlist.GiftlistScreen
import nz.ac.uclive.shopport.settings.SettingsScreen
import nz.ac.uclive.shopport.wishlist.WishlistScreen

object ShopportDestinations {
    const val WISHLIST_ROUTE = "wishlist"
    const val GIFTLIST_ROUTE = "giftlist"
    const val EXPLORE_ROUTE = "explore"
    const val SETTINGS_ROUTE = "settings"
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(navController = navController, startDestination = ShopportDestinations.WISHLIST_ROUTE) {
        composable(ShopportTabs.WISHLIST.route) {
            WishlistScreen(modifier = modifier, navController = navController)
        }
        composable(ShopportTabs.GIFTLIST.route) {
            GiftlistScreen(modifier = modifier, navController = navController)
        }
        composable(ShopportTabs.EXPLORE.route) {
            ExploreScreen(modifier = modifier, navController = navController)
        }
        composable(ShopportTabs.SETTINGS.route) {
            SettingsScreen(modifier = modifier)
        }
    }
}


enum class ShopportTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: String,
    val showInBottomBar: Boolean
) {
    WISHLIST(R.string.wishlist, R.drawable.ic_wishlist, ShopportDestinations.WISHLIST_ROUTE, true),
    GIFTLIST(R.string.giftlist, R.drawable.ic_gift, ShopportDestinations.GIFTLIST_ROUTE, true),
    EXPLORE(R.string.explore, R.drawable.ic_explore, ShopportDestinations.EXPLORE_ROUTE, true),
    SETTINGS(R.string.settings, R.drawable.ic_explore, ShopportDestinations.SETTINGS_ROUTE, false)
}
