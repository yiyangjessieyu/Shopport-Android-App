package nz.ac.uclive.shopport


import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import nz.ac.uclive.shopport.database.WishlistViewModel
import nz.ac.uclive.shopport.database.ShopportViewModelFactory
import nz.ac.uclive.shopport.database.WishListItem
import nz.ac.uclive.shopport.ui.theme.ShopportTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Shopport()
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shopport() {


    ShopportTheme {
        val tabs = remember { ShopportTabs.values() }
        val navController = rememberNavController()
        Scaffold(
            topBar = { ShopportAppBar()},
            bottomBar = { ShopportBottomBar(navController = navController, tabs) }
        ) { innerPaddingModifier ->
            NavigationHost(
                navController = navController,
                modifier = Modifier.padding(innerPaddingModifier)
            )
        }
    }
}

@Composable
fun ShopportBottomBar(navController: NavController, tabs: Array<ShopportTabs>) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ShopportTabs.GIFTLIST.route

    val routes = remember { ShopportTabs.values().map { it.route } }
    if (currentRoute in routes) {
        BottomAppBar {
            tabs.forEach { tab ->
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

@Preview
@Composable
fun AllPreview() {
    Shopport()
}
