package nz.ac.uclive.shopport.wishlist

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        topBar = { ShopportAppBar(navController = navController) },
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
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                )
            }
        }
    ) { innerPaddingModifier ->
        WishlistBody(modifier = modifier.padding(innerPaddingModifier), wishlistViewModel = wishlistViewModel)
    }
}
