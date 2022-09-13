package nz.ac.uclive.shopport.wishlist

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import nz.ac.uclive.shopport.WishlistItemClass
import nz.ac.uclive.shopport.wishlistItems


@Composable
fun WishlistBody(modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        items(wishlistItems, key = { it.id }) { item ->
            WishlistItem(item)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistItem(wishlistItem: WishlistItemClass) {
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