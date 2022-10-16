package nz.ac.uclive.shopport.giftlist

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.twotone.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import nz.ac.uclive.shopport.R
import nz.ac.uclive.shopport.ShopportDestinations
import nz.ac.uclive.shopport.common.camera.ComposeFileProvider
import nz.ac.uclive.shopport.common.location.LocationDetails
import nz.ac.uclive.shopport.database.GiftListItem
import nz.ac.uclive.shopport.database.GiftlistViewModel
import java.util.*
import kotlin.reflect.KFunction7


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddGiftlistItem(
    modifier: Modifier,
    navController: NavHostController,
    giftlistViewModel: GiftlistViewModel,
    location: LocationDetails?
) {
    var valid by rememberSaveable { mutableStateOf(false) }


    val newGiftListItem: GiftListItem by remember {
        mutableStateOf(
            GiftListItem(
                title = "",
                description = "",
                price = 0,
                imageURI = "",
                location = "",
                forPerson = "",
                forPersonColor = Color.White.toArgb()
            )
        )
    }

    fun setNewGiftListItem(
        title: String,
        description: String,
        price: Int,
        imageURI: String,
        location: String,
        forPerson: String,
        forPersonColor: Int
    ) {
        newGiftListItem.title = title
        newGiftListItem.description = description
        newGiftListItem.price = price
        newGiftListItem.imageURI = imageURI
        newGiftListItem.location = location
        newGiftListItem.forPerson = forPerson
        newGiftListItem.forPersonColor = forPersonColor

        valid = title.isNotBlank() && forPerson.isNotBlank()
    }

    fun addNewGiftlistItem() {
        Log.e("AddGiftlistItem", newGiftListItem.toString())
        giftlistViewModel.addGiftListItem(newGiftListItem)
    }

    Scaffold(
        topBar = {
            AddGiftlistItemTopBar(
                title = stringResource(R.string.add_to_giftlist),
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
                        addNewGiftlistItem()
                        navController.navigate(ShopportDestinations.GIFTLIST_ROUTE) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    },
                    enabled = valid
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.Save,
                        contentDescription = null,
                    )
                }

            }
        }
    ) { innerPaddingModifier ->
        AddToGiftlistBody(
            modifier = Modifier.padding(innerPaddingModifier),
            location = location,
            setNewGiftListItem = ::setNewGiftListItem,
            giftlistViewModel = giftlistViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddToGiftlistBody(
    modifier: Modifier,
    location: LocationDetails?,
    setNewGiftListItem: KFunction7<String, String, Int, String, String, String, Int, Unit>,
    giftlistViewModel: GiftlistViewModel,
) {
    val allPersons = giftlistViewModel.getAllForPersons().observeAsState(listOf()).value
    val rnd = Random()

    Log.d("AddToGiftlistBody", allPersons.toString())

    var titleText by rememberSaveable { mutableStateOf("") }
    var descriptionText by rememberSaveable { mutableStateOf("") }
    var priceText by rememberSaveable { mutableStateOf("") }
    var locationText by rememberSaveable { mutableStateOf("") }
    var imageURI by rememberSaveable { mutableStateOf<Uri?>(null) }
    var boughtBool by rememberSaveable { mutableStateOf(false) }
    var forPersonText by rememberSaveable { mutableStateOf("") }
    var forPersonColor = rememberSaveable { mutableStateOf(Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)).toArgb() ) }

    var isMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var showAddPerson by rememberSaveable { mutableStateOf(false) }
    var showColorPickerDialog = rememberSaveable { mutableStateOf(false) }

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

    fun updateGiftlistItem() {
        setNewGiftListItem(
            titleText,
            descriptionText,
            if (priceText.isNotEmpty()) priceText.toInt() else 0,
            if (imageURI !== null) imageURI.toString() else "",
            locationText,
            forPersonText,
            forPersonColor.value
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
        }
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            OutlinedTextField(
                value = titleText,
                label = {
                    Text(text = stringResource(R.string.name) + "*")
                },
                onValueChange = {
                    if (it.length <= 30) {
                        titleText = it
                        updateGiftlistItem()
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
                        updateGiftlistItem()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                                updateGiftlistItem()
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
                    Text(
                        text = stringResource(R.string.bought),
                        fontSize = 18.sp,
                        modifier = Modifier.alpha(0.8f)
                    )
                    Checkbox(
                        checked = boughtBool,
                        onCheckedChange = {
                            boughtBool = it
                            updateGiftlistItem()
                        },
                    )
                }
            }
        }


        item {
            if (!showAddPerson) {

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(
                        onClick = { isMenuExpanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (forPersonText.isEmpty()) {
                            Text(text = "${stringResource(R.string.choosePerson)}*")
                        } else {
                            Icon(
                                imageVector = Icons.TwoTone.Circle,
                                contentDescription = null,
                                tint = Color(giftlistViewModel.getColorForPerson(forPersonText))
                            )
                            Text(forPersonText, color = Color.White)
                        }
                    }

                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        allPersons.forEach { person ->
                            DropdownMenuItem(
                                onClick = {
                                    forPersonText = person
                                    forPersonColor.value =
                                        giftlistViewModel.getColorForPerson(forPersonText)
                                    updateGiftlistItem()
                                    isMenuExpanded = false
                                },
                                text = {
                                    Text(text = person)
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.TwoTone.Circle,
                                        contentDescription = null,
                                        tint = Color(
                                            giftlistViewModel.getColorForPerson(
                                                forPersonText
                                            )
                                        )
                                    )
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.addNew)) },
                            onClick = {
                                showAddPerson = true
                                isMenuExpanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.TwoTone.Add,
                                    contentDescription = null
                                )
                            }
                        )

                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = forPersonText,
                        label = {
                            Text(text = "${stringResource(R.string.receiver)}*")
                        },
                        onValueChange = {
                            if (it.length <= 30) {
                                forPersonText = it
                                updateGiftlistItem()
                            }
                        },
                        modifier = Modifier.weight(2f)
                    )
                    TextButton(
                        onClick = { showColorPickerDialog.value = true },
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.TwoTone.Circle,
                            contentDescription = null,
                            tint = Color(forPersonColor.value)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.colour),
                            color = Color(forPersonColor.value),
                            fontSize = 18.sp
                        )
                    }
                }
            }

        }

        // IMAGE INPUT
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
                        updateGiftlistItem()
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
    if (showColorPickerDialog.value) {
        ColorPickerDialog(showColorPickerDialog, forPersonText, forPersonColor, ::updateGiftlistItem)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerDialog(
    showColorPickerDialog: MutableState<Boolean>,
    forPersonText: String,
    forPersonColor: MutableState<Int>,
    updateGiftlistItem: () -> Unit
) {
    val controller = rememberColorPickerController()

    Dialog(onDismissRequest = { showColorPickerDialog.value = false }) {

        Card(
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            fontSize = 24.sp,
                            text = stringResource(R.string.chooseColour)
                        )
                        Text(
                            fontSize = 16.sp,
                            text = forPersonText,
                            modifier = Modifier.alpha(0.5f)
                        )
                    }
                    IconButton(modifier = Modifier.then(Modifier.size(24.dp)),
                        onClick = {
                            showColorPickerDialog.value = false
                        }) {
                        Icon(
                            Icons.Filled.Close,
                            null
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 16.dp)
                ) {

                    HsvColorPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(10.dp),
                        controller = controller,
                        onColorChanged = { colorEnvelope: ColorEnvelope ->
                            forPersonColor.value = colorEnvelope.color.toArgb()
                            updateGiftlistItem()
                        }
                    )

                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 16.dp)
                ) {

                    TextButton(
                        onClick = {
                            showColorPickerDialog.value = false
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .padding(4.dp, 2.dp)
                            .width(100.dp)

                    ) {
                        Text(text = stringResource(R.string.save), fontSize = 20.sp)
                    }

                }
            }
        }


    }
}
