package nz.ac.uclive.shopport.common

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import nz.ac.uclive.shopport.ShopportDestinations
import nz.ac.uclive.shopport.database.WishListItem
import nz.ac.uclive.shopport.database.WishlistViewModel
import kotlin.reflect.KFunction0

@Composable
fun AddWishlistItemTopBar(
    title: String,
    navController: NavHostController,
    valid: Boolean,
    addNewItem: KFunction0<Unit>
) {

    val prev = navController.previousBackStackEntry?.destination?.route ?: ShopportDestinations.WISHLIST_ROUTE

    TopAppBar(
        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        elevation = 8.dp
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                    addNewItem()
                    navController.navigate(prev) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                },
                enabled = valid
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = null)
            }
        }


    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    }

}