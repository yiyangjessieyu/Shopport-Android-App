package nz.ac.uclive.shopport.wishlist

import android.util.Log
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import nz.ac.uclive.shopport.R
import nz.ac.uclive.shopport.ShopportDestinations
import nz.ac.uclive.shopport.common.AddItemTopBar
import nz.ac.uclive.shopport.common.camera.CameraView


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWishlistItem(modifier: Modifier, navController: NavHostController) {

    var valid = remember { mutableStateOf(false) }
    Scaffold(
        topBar = { AddItemTopBar(title = stringResource(R.string.add_to_wishlist), navController = navController, valid = valid.value) },
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
        AddToWishlistBody(modifier = Modifier.padding(innerPaddingModifier), valid = valid)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToWishlistBody(modifier: Modifier, valid: MutableState<Boolean>) {
    var titleText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var locationText by remember { mutableStateOf("") }
    var imageIdText by remember { mutableStateOf("") }
    var boughtBool by remember { mutableStateOf(false) }

    var showCamera by remember { mutableStateOf(false) }
    fun toggleCamera() {
        showCamera = !showCamera
    }

    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = titleText,
            label = {
                Text(text = stringResource(R.string.name) + "*")
            },
            onValueChange = {
                if (it.length <= 30) {
                    titleText = it
                    valid.value = it.isNotBlank()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descriptionText,
            maxLines = 3,
            label = {
                Text(text = stringResource(R.string.description))
            },
            onValueChange = {
                if (it.length <= 100) {
                    descriptionText = it
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

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
                    priceText = if (it.startsWith("0")) {
                        ""
                    } else {
                        it
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
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = { showCamera = !showCamera }) {
                Icon(Icons.TwoTone.Image, contentDescription = null)
                Text(
                    text = if (!showCamera) stringResource(R.string.addImage) else stringResource(R.string.takeAnotherImage),
                    fontSize = 18.sp, modifier = Modifier.padding(start = 8.dp))
            }
        }
        if (showCamera) {
            Spacer(modifier = Modifier.height(6.dp))
            Dialog(onDismissRequest = {}) {
                Box(
                ) {
                    CameraView(
                        onImageCaptured = { uri, fromGallery ->
                            Log.d("CAMERA", "Image Uri Captured from Camera View")
                            //Todo : use the uri as needed
                            Log.e("CAMERA", uri.toString())
                            showCamera = false
                        }, onError = { imageCaptureException ->
                            imageCaptureException.printStackTrace()
                        }
                    )
                }
            }
        }

    }
}
