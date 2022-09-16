package nz.ac.uclive.shopport.wishlist

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AttachMoney
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.LocationOn
import androidx.compose.material.icons.twotone.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import nz.ac.uclive.shopport.database.WishListItem
import nz.ac.uclive.shopport.database.WishlistViewModel
import nz.ac.uclive.shopport.ui.theme.md_theme_bought_container
import nz.ac.uclive.shopport.ui.theme.md_theme_on_bought_container


@Composable
fun WishlistBody(modifier: Modifier, wishlistViewModel: WishlistViewModel) {

    val items = wishlistViewModel.wishListItems.observeAsState(listOf()).value

    LazyColumn(modifier = modifier) {
        // TODO remove this later
        item {
            Row {
                Button(onClick = {
                    wishlistViewModel.addWishListItem(
                        WishListItem(
                            title = "Apple AirPods Pro",
                            description = "Apple AirPods Pro",
                            price = 249.00,
                            location = "-43.5356073,172.5807374",
                            imageURI = "https://picsum.photos/400/400",
                            bought = true
                        )
                    )
                }) {
                    Text(text = "Add item")
                }
                Button(onClick = {
                    wishlistViewModel.deleteAllItems()
                }) {
                    Text(text = "Delete all")
                }
            }
        }
        items(items, key = { it.id }) { item ->
            WishlistItem(item, wishlistViewModel::deleteWishListItem, wishlistViewModel = wishlistViewModel)
        }
        item {
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistItem(
    wishlistItem: WishListItem,
    deleteItem: (WishListItem) -> Unit,
    wishlistViewModel: WishlistViewModel
) {
    var bought by remember { mutableStateOf(wishlistItem.bought) }

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .padding(8.dp, 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(text = wishlistItem.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(text = wishlistItem.description, style = MaterialTheme.typography.titleSmall, modifier = Modifier.alpha(0.75f))
            }
            IconButton(onClick = { deleteItem(wishlistItem) }) {
                Icon(imageVector = Icons.TwoTone.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
            }
        }
        Log.e("WishlistItem", wishlistItem.imageURI)
        if (wishlistItem.imageURI.isNotEmpty()) {
            AsyncImage(
                model = wishlistItem.imageURI,
                contentDescription = null,
                modifier = Modifier.requiredHeight(300.dp),
                contentScale = ContentScale.Crop,
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(60.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            ElevatedCard {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(12.dp, 4.dp), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Icon(imageVector = Icons.TwoTone.AttachMoney, contentDescription = null, tint = MaterialTheme.colorScheme.surfaceTint)
                    Text(text = String.format("%.2f", wishlistItem.price), fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }
            }

            ElevatedFilterChip(
                selected = bought,
                onClick = {
                    bought = !bought
                    wishlistItem.bought = !wishlistItem.bought
                    wishlistViewModel.updateWishListItem(wishlistItem)
                },
                label = { Text(text = "Bought", fontWeight = FontWeight.Bold, modifier = Modifier.background(
                    Color.Transparent), fontSize = 24.sp) },
                colors = FilterChipDefaults.elevatedFilterChipColors(
                    selectedContainerColor = md_theme_bought_container,
                    selectedLabelColor = md_theme_on_bought_container,
                ),
            )

            Row {
                val context = LocalContext.current
                IconButton(onClick = {
                    val gmmIntentUri = Uri.parse("geo:${wishlistItem.location}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    ContextCompat.startActivity(context, mapIntent, null)
                }) {
                    Icon(imageVector = Icons.TwoTone.LocationOn, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.TwoTone.Share, contentDescription = null)
                }
            }
        }

    }

}