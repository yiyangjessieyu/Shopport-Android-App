package nz.ac.uclive.shopport

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import nz.ac.uclive.shopport.wishlist.WishlistScreen

private object ShopportDestinations {
    const val WISHLIST_ROUTE = "wishlist"
    const val GIFTLIST_ROUTE = "giftlist"
    const val EXPLORE_ROUTE = "explore"
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {


    NavHost(navController = navController, startDestination = ShopportDestinations.WISHLIST_ROUTE) {
        composable(ShopportTabs.WISHLIST.route) {
            WishlistScreen(modifier = modifier)
//            Scaffold(
//                floatingActionButton = {
//                    FloatingActionButton(
//                        onClick = { /*TODO*/ },
//                        modifier = modifier.size(56.dp),
//                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Edit,
//                            contentDescription =  null,
//                        )
//                    }
//                }
//            ) {
//                Text(text = "Wishlist", modifier = modifier)
//            }

        }
        composable(ShopportTabs.GIFTLIST.route) {
            Text(text = "Giftlist", modifier = modifier)
        }
        composable(ShopportTabs.EXPLORE.route) {
            Text(text = "Explore", modifier = modifier)
        }
    }
}

@Composable
fun ShopportAppBar() {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier.height(120.dp),
        backgroundColor = MaterialTheme.colorScheme.background,
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_shopport_logo),
                contentDescription = null
            )
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { /* todo */ }
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(R.string.settings)
                )
            }
        }
    }
}

enum class ShopportTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: String
) {
    WISHLIST(R.string.wishlist, R.drawable.ic_wishlist, ShopportDestinations.WISHLIST_ROUTE),
    GIFTLIST(R.string.giftlist, R.drawable.ic_gift, ShopportDestinations.GIFTLIST_ROUTE),
    EXPLORE(R.string.explore, R.drawable.ic_explore, ShopportDestinations.EXPLORE_ROUTE)
}

@Preview
@Composable
fun ShopportAppBarPreview() {
    ShopportAppBar()
}