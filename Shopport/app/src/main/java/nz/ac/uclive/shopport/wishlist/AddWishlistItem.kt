package nz.ac.uclive.shopport.wishlist

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AttachMoney
import androidx.compose.material.icons.twotone.Image
import androidx.compose.material.icons.twotone.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import nz.ac.uclive.shopport.R
import nz.ac.uclive.shopport.ShopportDestinations
import nz.ac.uclive.shopport.common.AddWishlistItemTopBar
import nz.ac.uclive.shopport.common.camera.CameraView
import nz.ac.uclive.shopport.common.location.LocationDetails
import nz.ac.uclive.shopport.database.WishListItem
import nz.ac.uclive.shopport.database.WishlistViewModel
import kotlin.reflect.KFunction5


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWishlistItem(
    modifier: Modifier,
    navController: NavHostController,
    wishlistViewModel: WishlistViewModel,
    location: LocationDetails?
) {
    val valid = remember { mutableStateOf(false) }
    val newWishListItem : WishListItem by remember { mutableStateOf(WishListItem(
        title = "",
        description = "",
        price = 0.0,
        imageURI = "",
        location = ""
    )) }

    fun setNewWishListItem(title: String, description: String, price: Double, imageURI: String, location: String) {
        newWishListItem.title = title
        newWishListItem.description = description
        newWishListItem.price = price
        newWishListItem.imageURI = imageURI
        newWishListItem.location = location
    }

    fun addNewWishlistItem() {
        Log.e("AddWishlistItem", newWishListItem.toString())
        wishlistViewModel.addWishListItem(newWishListItem)
    }

    Scaffold(
        topBar = { AddWishlistItemTopBar(
            title = stringResource(R.string.add_to_wishlist),
            navController = navController,
            valid = valid.value,
            addNewItem = ::addNewWishlistItem
        )
     },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ShopportDestinations.WISHLIST_ROUTE) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                },
                modifier = modifier.size(56.dp),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            ) {
                Icon(
                    imageVector = Icons.TwoTone.KeyboardArrowDown,
                    contentDescription = null,
                )
            }
        }
    ) { innerPaddingModifier ->
        AddToWishlistBody(
            modifier = Modifier.padding(innerPaddingModifier),
            valid = valid,
            location = location,
            setNewWishListItem = ::setNewWishListItem
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToWishlistBody(
    modifier: Modifier,
    valid: MutableState<Boolean>,
    location: LocationDetails?,
    setNewWishListItem: KFunction5<String, String, Double, String, String, Unit>,
) {
    var titleText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var locationText by remember { mutableStateOf("") }
    var imageURI by remember { mutableStateOf("") }
    var boughtBool by remember { mutableStateOf(false) }

    var showCamera by remember { mutableStateOf(false) }

    if (location != null) {
        locationText = location.string
    }

    fun updateWishlistItem() {
        setNewWishListItem(titleText, descriptionText, if (priceText.isNotEmpty()) priceText.toDouble() else 0.0, imageURI, locationText)
    }

    LazyColumn(modifier = modifier
        .fillMaxSize()
        .padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        item {
            OutlinedTextField(
                value = titleText,
                label = {
                    Text(text = stringResource(R.string.name) + "*")
                },
                onValueChange = {
                    if (it.length <= 30) {
                        titleText = it
                        valid.value = it.isNotBlank()
                        updateWishlistItem()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = descriptionText,
                maxLines = 3,
                label = {
                    Text(text = stringResource(R.string.description))
                },
                onValueChange = {
                    if (it.length <= 100) {
                        descriptionText = it
                        updateWishlistItem()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = priceText,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.TwoTone.AttachMoney,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = stringResource(R.string.price))
                    },
                    onValueChange = {
                        if (it.length <= 5) {
                            if (it.startsWith("0")) {
                                priceText = ""
                            } else {
                                priceText = it
                                updateWishlistItem()
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    ),
                    modifier = Modifier.weight(1f)
                )

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(R.string.bought), fontSize = 18.sp, modifier = Modifier.alpha(0.8f))
                    Checkbox(
                        checked = boughtBool,
                        onCheckedChange = {
                            boughtBool = it
                        },
                    )
                    updateWishlistItem()
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(onClick = { showCamera = !showCamera }) {
                    Icon(Icons.TwoTone.Image, contentDescription = null)
                    Text(
                        text = if (imageURI.isBlank()) stringResource(R.string.addImage) else stringResource(R.string.changeImage),
                        fontSize = 18.sp, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        item {
            Log.e("ImageURI", imageURI)
            if (imageURI.isNotBlank()) {
                AsyncImage(
                    model = imageURI,
                    contentDescription = null,
                    modifier = Modifier.requiredHeight(300.dp),
                    contentScale = ContentScale.Crop,
                )
            }
        }

        item {

            if (showCamera) {
                Spacer(modifier = Modifier.height(6.dp))
                Dialog(onDismissRequest = {}) {
                    Box {
                        CameraView(
                            onImageCaptured = { uri, _ ->
                                Log.d("CAMERA", "Image Uri Captured from Camera View")
                                showCamera = false
                                imageURI = uri.toString()
                                updateWishlistItem()
                            }, onError = { imageCaptureException ->
                                imageCaptureException.printStackTrace()
                                showCamera = false
                            },
                            onClose = {
                                showCamera = false
                            }
                        )
                    }
                }
            }

        }

    }
}
