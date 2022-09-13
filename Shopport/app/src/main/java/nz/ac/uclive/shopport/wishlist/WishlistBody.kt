package nz.ac.uclive.shopport.wishlist

import android.app.Application
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import nz.ac.uclive.shopport.database.ShopportViewModelFactory
import nz.ac.uclive.shopport.database.WishListItem
import nz.ac.uclive.shopport.database.WishlistViewModel


@Composable
fun WishlistBody(modifier: Modifier) {

    val context = LocalContext.current
    val wishlistViewModel: WishlistViewModel = viewModel(
        factory = ShopportViewModelFactory(context.applicationContext as Application)
    )
    val items = wishlistViewModel.wishListItems.observeAsState(listOf()).value

    LazyColumn(modifier = modifier) {
        item {
            Button(onClick = {
                wishlistViewModel.addWishListItem(
                    WishListItem(
                        title = "Apple AirPods Pro",
                        description = "Apple AirPods Pro",
                        price = 249,
                        location = "NZ",
                        imageId = "https://picsum.photos/200/300",
                        bought = true
                    )
                )
            }) {
                Text(text = "Add item")
            }
        }
        items(items, key = { it.id }) { item ->
            WishlistItem(item)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistItem(wishlistItem: WishListItem) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
        }

    }
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(wishlistItem.imageId)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
//                modifier = Modifier.clip(SquareShape)
    )
}

@Preview
@Composable
fun WishlistBodyPreview() {
    WishlistBody(modifier = Modifier)
}