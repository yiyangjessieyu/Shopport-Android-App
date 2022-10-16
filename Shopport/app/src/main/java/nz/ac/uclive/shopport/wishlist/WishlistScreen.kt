package nz.ac.uclive.shopport.wishlist

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import nz.ac.uclive.shopport.ShopportDestinations
import nz.ac.uclive.shopport.common.ShopportAppBar
import nz.ac.uclive.shopport.database.WishlistViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    modifier: Modifier,
    navController: NavHostController,
    wishlistViewModel: WishlistViewModel
) {
    Scaffold(
        topBar = {
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                ShopportAppBar(navController = navController)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ShopportDestinations.ADD_WISHLIST_ROUTE) {
                        popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                        }
                    }
                },
                modifier = modifier.size(56.dp),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        }
    ) { innerPaddingModifier ->
        WishlistBody(modifier = modifier.padding(innerPaddingModifier), wishlistViewModel = wishlistViewModel)
    }
}
