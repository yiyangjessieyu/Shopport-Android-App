package nz.ac.uclive.shopport

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import nz.ac.uclive.shopport.common.LocationViewModel
import nz.ac.uclive.shopport.database.ShopportViewModelFactory
import nz.ac.uclive.shopport.database.WishlistViewModel
import nz.ac.uclive.shopport.explore.ExploreScreen
import nz.ac.uclive.shopport.explore.ShopViewModel
import nz.ac.uclive.shopport.giftlist.GiftlistScreen
import nz.ac.uclive.shopport.settings.SettingsScreen
import nz.ac.uclive.shopport.wishlist.AddWishlistItem
import nz.ac.uclive.shopport.wishlist.WishlistScreen
import java.util.*

object ShopportDestinations {
    const val WISHLIST_ROUTE = "wishlist"
    const val ADD_WISHLIST_ROUTE = "addWishlist"
    const val GIFTLIST_ROUTE = "giftlist"
    const val EXPLORE_ROUTE = "explore"
    const val SETTINGS_ROUTE = "settings"
}

enum class ShopportScreens(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: String,
    val showInBottomBar: Boolean
) {
    WISHLIST(R.string.wishlist, R.drawable.ic_wishlist, ShopportDestinations.WISHLIST_ROUTE, true),
    ADD_WISHLIST(R.string.wishlist, R.drawable.ic_wishlist, ShopportDestinations.ADD_WISHLIST_ROUTE, false),
    GIFTLIST(R.string.giftlist, R.drawable.ic_gift, ShopportDestinations.GIFTLIST_ROUTE, true),
    EXPLORE(R.string.explore, R.drawable.ic_explore, ShopportDestinations.EXPLORE_ROUTE, true),
    SETTINGS(R.string.settings, R.drawable.ic_explore, ShopportDestinations.SETTINGS_ROUTE, false)
}
val tweenSpec = tween<IntOffset>(durationMillis = 700, easing = FastOutSlowInEasing)


@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val shopViewModel = ShopViewModel()
    val wishlistViewModel: WishlistViewModel = viewModel(
        factory = ShopportViewModelFactory(context.applicationContext as Application)
    )
    val locationViewModel = LocationViewModel(context)
    locationViewModel.startLocationUpdates()
    val location by locationViewModel.getLocationLiveData().observeAsState()


    AnimatedNavHost(navController = navController, startDestination = ShopportDestinations.WISHLIST_ROUTE) {
        composable(ShopportScreens.WISHLIST.route) {
            WishlistScreen(modifier = modifier, navController = navController, wishlistViewModel = wishlistViewModel)
        }
        composable(ShopportScreens.GIFTLIST.route) {
            GiftlistScreen(modifier = modifier, navController = navController)
        }
        composable(ShopportScreens.EXPLORE.route) {
            ExploreScreen(modifier = modifier, navController = navController, vm = shopViewModel)
        }
        composable(
            ShopportScreens.SETTINGS.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(600))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(300))
            }
        ) {
            SettingsScreen(modifier = modifier)
        }
        composable(
            ShopportScreens.ADD_WISHLIST.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tweenSpec)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tweenSpec)
            }
        ) {
            AddWishlistItem(modifier = modifier, navController = navController, wishlistViewModel = wishlistViewModel, location = location)
        }
    }
}

@Composable
fun ShopportBottomBar(navController: NavController, tabs: Array<ShopportScreens>) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ShopportScreens.GIFTLIST.route

    val routes = remember { ShopportScreens.values().map { it.route } }
    if (currentRoute in routes) {
        BottomAppBar {
            tabs.forEach { tab ->
                if (tab.showInBottomBar) {
                    NavigationBarItem(
                        icon = { Icon(painterResource(tab.icon), contentDescription = null) },
                        label = { Text(stringResource(tab.title).uppercase(Locale.getDefault())) },
                        selected = currentRoute == tab.route,
                        onClick = {
                            if (tab.route != currentRoute) {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        alwaysShowLabel = false,
                    )
                }
            }
        }
    }
}
