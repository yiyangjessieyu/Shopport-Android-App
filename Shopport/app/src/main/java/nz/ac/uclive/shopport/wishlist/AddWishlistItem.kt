package nz.ac.uclive.shopport.wishlist

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AttachMoney
import androidx.compose.material.icons.twotone.Image
import androidx.compose.material.icons.twotone.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import nz.ac.uclive.shopport.R
import nz.ac.uclive.shopport.ShopportDestinations
import nz.ac.uclive.shopport.common.camera.ComposeFileProvider
import nz.ac.uclive.shopport.common.location.LocationDetails
import nz.ac.uclive.shopport.database.WishListItem
import nz.ac.uclive.shopport.database.WishlistViewModel
import nz.ac.uclive.shopport.giftlist.AddWishlistItemTopBar
import kotlin.reflect.KFunction5


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWishlistItem(
    modifier: Modifier,
    navController: NavHostController,
    wishlistViewModel: WishlistViewModel,
    location: LocationDetails?
) {
    val valid = rememberSaveable { mutableStateOf(false) }
    val newWishListItem : WishListItem by remember { mutableStateOf(WishListItem(
        title = "",
        description = "",
        price = 0,
        imageURI = "",
        location = ""
    )) }

    fun setNewWishListItem(title: String, description: String, price: Int, imageURI: String, location: String) {
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
        )
     },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                },
                modifier = modifier.size(56.dp),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            ) {
                IconButton(
                    onClick = {
                        addNewWishlistItem()
                        navController.navigate(ShopportDestinations.WISHLIST_ROUTE) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }},
                    enabled = valid.value
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.Save,
                        contentDescription = null,
                    )
                }

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddToWishlistBody(
    modifier: Modifier,
    valid: MutableState<Boolean>,
    location: LocationDetails?,
    setNewWishListItem: KFunction5<String, String, Int, String, String, Unit>,
) {
    var titleText by rememberSaveable { mutableStateOf("") }
    var descriptionText by rememberSaveable { mutableStateOf("") }
    var priceText by rememberSaveable { mutableStateOf("") }
    var locationText by rememberSaveable { mutableStateOf("") }
    var imageURI by rememberSaveable { mutableStateOf<Uri?>(null) }
    var boughtBool by rememberSaveable { mutableStateOf(false) }

    var hasImage by rememberSaveable {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    if (location != null) {
        locationText = location.string
    }

    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )

    fun updateWishlistItem() {
        setNewWishListItem(
            titleText,
            descriptionText,
            if (priceText.isNotEmpty()) priceText.strip().toInt() else 0,
            if (imageURI !== null) imageURI.toString() else "",
            locationText
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
        }
    )

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
                            updateWishlistItem()
                        },
                    )
                }
            }
        }

        if (cameraPermissionState.hasPermission) {
            item {
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(onClick = {
                        val uri = ComposeFileProvider.getImageUri(context)
                        imageURI = uri
                        cameraLauncher.launch(uri)
                        updateWishlistItem()
                    }) {
                        Icon(Icons.TwoTone.Image, contentDescription = null)
                        Text(
                            text = if (imageURI == null) stringResource(R.string.addImage) else stringResource(
                                R.string.changeImage
                            ),
                            fontSize = 18.sp, modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            item {
                if (hasImage && imageURI != null) {
                    AsyncImage(
                        model = imageURI,
                        contentDescription = null,
                        modifier = Modifier.requiredHeight(300.dp),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }

    }
}
