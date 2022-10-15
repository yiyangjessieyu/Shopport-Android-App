package nz.ac.uclive.shopport.wishlist

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.twotone.AttachMoney
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.LocationOn
import androidx.compose.material.icons.twotone.Share
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import nz.ac.uclive.shopport.R
import nz.ac.uclive.shopport.database.WishListItem
import nz.ac.uclive.shopport.database.WishlistViewModel
import nz.ac.uclive.shopport.ui.theme.md_theme_bought_container
import nz.ac.uclive.shopport.ui.theme.md_theme_on_bought_container


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun WishlistBody(modifier: Modifier, wishlistViewModel: WishlistViewModel) {

    val items = wishlistViewModel.wishListItems.observeAsState(listOf()).value

    LazyColumn(modifier = modifier) {

        items(items, key = { it.id }) { item ->

            val dismissState = rememberDismissState()
            if (dismissState.isDismissed(DismissDirection.EndToStart) || dismissState.isDismissed(DismissDirection.StartToEnd)) {
                wishlistViewModel.deleteWishListItem(item)
            }

            SwipeToDismiss(
                state = dismissState,
                modifier = Modifier
                    .padding(16.dp, 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .animateItemPlacement(),
                directions = setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd),
                background = {
                    val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                    val color by animateColorAsState(
                        when (dismissState.targetValue) {
                            DismissValue.Default -> MaterialTheme.colorScheme.background
                            DismissValue.DismissedToEnd -> Color(0xFF690005)
                            DismissValue.DismissedToStart -> Color(0xFF690005)
                        }
                    )
                    val alignment = when (direction) {
                        DismissDirection.StartToEnd -> Alignment.CenterStart
                        DismissDirection.EndToStart -> Alignment.CenterEnd
                    }
                    val icon = when (direction) {
                        DismissDirection.StartToEnd -> Icons.TwoTone.Delete
                        DismissDirection.EndToStart -> Icons.TwoTone.Delete
                    }

                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(horizontal = 20.dp),
                        contentAlignment = alignment
                    ) {
                        Row(Modifier.padding(0.dp), verticalAlignment = Alignment.CenterVertically) {
                            androidx.compose.material.Icon(
                                icon,
                                contentDescription = "Localized description",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.onError,
                                        shape = CircleShape
                                    )
                                    .padding(8.dp)
                            )
                            androidx.compose.material.Divider(color = MaterialTheme.colorScheme.onError, thickness = 4.dp)
                        }
                    }
                },
                dismissContent = { WishlistItem(item, wishlistViewModel::deleteWishListItem, wishlistViewModel = wishlistViewModel)}
            )

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
    val bought = remember { mutableStateOf(wishlistItem.bought) }
    val expanded = remember { mutableStateOf(false) }
    val expandIconRotationState by animateFloatAsState(targetValue = if (expanded.value) 180f else 0f)

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        WishlistBodyHeader(wishlistItem = wishlistItem, expanded, expandIconRotationState, deleteItem)

        Log.e("Image", wishlistItem.imageURI)
        AnimatedVisibility(expanded.value) {
            AsyncImage(
                model = wishlistItem.imageURI,
                contentDescription = null,
                modifier = Modifier.requiredHeight(300.dp),
                contentScale = ContentScale.Crop,
            )
        }

        WishlistBodyFooter(wishlistItem = wishlistItem, wishlistViewModel = wishlistViewModel, bought)

    }

}

@Composable
fun WishlistBodyHeader(
    wishlistItem: WishListItem,
    expanded: MutableState<Boolean>,
    expandIconRotationState: Float,
    deleteItem: (WishListItem) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(12.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(text = wishlistItem.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = wishlistItem.description, style = MaterialTheme.typography.titleSmall, modifier = Modifier.alpha(0.75f))
        }
        Row {
            if (wishlistItem.imageURI.isNotEmpty()) {
                IconButton(
                    onClick = { expanded.value = !expanded.value },
                    modifier = Modifier
                        .alpha(ContentAlpha.medium)
                        .rotate(expandIconRotationState)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                    )
                }
            }

            IconButton(onClick = { deleteItem(wishlistItem) }) {
                Icon(
                    imageVector = Icons.TwoTone.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistBodyFooter(
    wishlistItem: WishListItem,
    wishlistViewModel: WishlistViewModel,
    bought: MutableState<Boolean>,
) {
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
                Text(text = wishlistItem.price.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }

        ElevatedFilterChip(
            selected = bought.value,
            onClick = {
                bought.value = !bought.value
                wishlistItem.bought = !wishlistItem.bought
                wishlistViewModel.updateWishListItem(wishlistItem)
            },
            label = {
                Text(
                    text = stringResource(id = R.string.bought),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.background(Color.Transparent),
                    fontSize = 24.sp
                )
            },
            colors = FilterChipDefaults.elevatedFilterChipColors(
                selectedContainerColor = md_theme_bought_container,
                selectedLabelColor = md_theme_on_bought_container,
            ),
        )

        Row {
            if (wishlistItem.location.isNotBlank() && wishlistItem.location != ",") {
                val context = LocalContext.current
                IconButton(onClick = {
                    val gmmIntentUri = Uri.parse("geo:${wishlistItem.location}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    startActivity(context, mapIntent, null)
                }) {
                    Icon(imageVector = Icons.TwoTone.LocationOn, contentDescription = null)
                }
            }

            Spacer(modifier = Modifier.width(4.dp))
            ShareButton(wishlistItem)
        }
    }
}

@Composable
fun ShareButton(wishlistItem: WishListItem) {
    val context = LocalContext.current

    fun shareImage(imageURI: Uri) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"
        val textToShare = "Test text"
        share.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        share.putExtra(Intent.EXTRA_STREAM, imageURI)
        share.putExtra(Intent.EXTRA_TEXT, textToShare)
        share.putExtra(Intent.EXTRA_SUBJECT, "Text subject")
        startActivity(context, Intent.createChooser(share, "Share image"), null)
    }

    // Turn string uri to uri object
    val uri = Uri.parse(wishlistItem.imageURI)
    IconButton(onClick = {
        shareImage(uri)
    }) {
        Icon(imageVector = Icons.TwoTone.Share, contentDescription = null)
    }
}
