package nz.ac.uclive.shopport.wishlist

import android.app.Application
import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AttachMoney
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.LocationOn
import androidx.compose.material.icons.twotone.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import nz.ac.uclive.shopport.database.ShopportViewModelFactory
import nz.ac.uclive.shopport.database.WishListItem
import nz.ac.uclive.shopport.database.WishListItemDao
import nz.ac.uclive.shopport.database.WishlistViewModel


@Composable
fun WishlistBody(modifier: Modifier) {

    val context = LocalContext.current
    val wishlistViewModel: WishlistViewModel = viewModel(
        factory = ShopportViewModelFactory(context.applicationContext as Application)
    )
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
                            location = "NZ",
                            imageId = "https://picsum.photos/400/400",
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
            WishlistItem(item, wishlistViewModel::deleteWishListItem)
        }
        item {
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistItem(wishlistItem: WishListItem, deleteItem: (WishListItem) -> Unit) {
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

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(wishlistItem.imageId)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(60.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElevatedAssistChip(
                onClick = {},
                leadingIcon = {
                    Icon(imageVector = Icons.TwoTone.AttachMoney, contentDescription = null)
                },
                label = {
                    Text(
                        text = String.format("%.2f", wishlistItem.price),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                },
            )

            Row {
                IconButton(onClick = { /*TODO*/ }) {
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