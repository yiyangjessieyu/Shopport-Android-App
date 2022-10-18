package nz.ac.uclive.shopport.date

import nz.ac.uclive.shopport.giftlist.AddGiftlistItemTopBar

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import nz.ac.uclive.shopport.R
import nz.ac.uclive.shopport.ShopportDestinations
import nz.ac.uclive.shopport.common.camera.ComposeFileProvider
import nz.ac.uclive.shopport.database.DateItem
import nz.ac.uclive.shopport.database.GiftlistViewModel
import java.util.*
import kotlin.reflect.KFunction6


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddDateItem(
    modifier: Modifier,
    navController: NavHostController,
    giftlistViewModel: GiftlistViewModel
) {
    var valid by rememberSaveable { mutableStateOf(false) }


    val newDateItem: DateItem by remember {
        mutableStateOf(
            DateItem(
                title = "",
                description = "",
                forPerson = "",
                forPersonColor = Color.White.toArgb(),
                month = 0,
                dayOfMonth = 0
            )
        )
    }

    fun setNewDateItem(
        title: String,
        description: String,
        forPerson: String,
        forPersonColor: Int,
        month: Int,
        dayOfMonth: Int
    ) {
        newDateItem.title = title
        newDateItem.description = description
        newDateItem.forPerson = forPerson
        newDateItem.forPersonColor = forPersonColor
        newDateItem.month = month
        newDateItem.dayOfMonth = dayOfMonth

        valid = title.isNotBlank() && forPerson.isNotBlank()
    }

//    fun addNewGiftlistItem() {
//        Log.e("AddGiftlistItem", newDateItem.toString())
//        giftlistViewModel.addGiftListItem(newDateItem)
//    }

    Scaffold(
        topBar = {
            AddGiftlistItemTopBar(
                title = stringResource(R.string.add_personal_notifications),
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
//                        addNewGiftlistItem() TODO set up notification as the function being triggered
                        navController.navigate(ShopportDestinations.ADD_DATE_ROUTE) {
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
        AddDateBody(
            modifier = Modifier.padding(innerPaddingModifier),
            setNewDateItem = ::setNewDateItem,
            giftlistViewModel = giftlistViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddDateBody(
    modifier: Modifier,
    setNewDateItem: KFunction6<String, String, String, Int, Int, Int, Unit>,
    giftlistViewModel: GiftlistViewModel,
) {
    val allPersons = giftlistViewModel.getAllForPersons().observeAsState(listOf()).value
    val rnd = Random()

    Log.d("AddDateBody: allPersons.toString()", allPersons.toString())

    var titleText by rememberSaveable { mutableStateOf("") }
    var descriptionText by rememberSaveable { mutableStateOf("") }
    var forPersonText by rememberSaveable { mutableStateOf("") }
    var forPersonColor = rememberSaveable { mutableStateOf(Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)).toArgb() ) }
    var month by rememberSaveable { mutableStateOf(0) }
    var dayOfMonth by rememberSaveable { mutableStateOf(0) }

    var colorARGB = giftlistViewModel.getColorForPerson(forPersonText).observeAsState(Color.White.toArgb())

    var isMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var showAddPerson by rememberSaveable { mutableStateOf(false) }
    var showColorPickerDialog = rememberSaveable { mutableStateOf(false) }
    var showDateDialog = rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    fun updateDateItem() {
        setNewDateItem(
            titleText,
            descriptionText,
            forPersonText,
            forPersonColor.value,
            month,
            dayOfMonth
        )
    }

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
                        updateDateItem()
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
                        updateDateItem()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
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
                                tint = Color(colorARGB.value)
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
                            val colorForPerson = giftlistViewModel.getColorForPerson(person).observeAsState(Color.White.toArgb()).value

                            DropdownMenuItem(
                                onClick = {
                                    forPersonText = person
                                    forPersonColor.value = colorForPerson
                                    updateDateItem()
                                    isMenuExpanded = false
                                },
                                text = {
                                    Text(text = person)
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.TwoTone.Circle,
                                        contentDescription = null,
                                        tint = Color(colorForPerson)
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
                            Text(text = "${stringResource(R.string.important_date_for)}*")
                        },
                        onValueChange = {
                            if (it.length <= 30) {
                                forPersonText = it
                                updateDateItem()
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

        item {
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedButton(
                    onClick = {
                        showDateDialog.value = true
                        updateDateItem()
//                        val uri = ComposeFileProvider.getImageUri(context)
//                        imageURI = uri TODO
                    }
                ) {
                    Icon(Icons.TwoTone.Event, contentDescription = null)
                    Text(
                        text = stringResource(R.string.date
//                        if (imageURI == null) stringResource(R.string.addImage) else TODO
                        ),
                        fontSize = 18.sp, modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

    }
    if (showColorPickerDialog.value) {
        ColorPickerDialog(showColorPickerDialog, forPersonText, forPersonColor, ::updateDateItem)
    }

    if (showDateDialog.value) {
        CustomDatePickerDialog(label = "Important Date Picker") {
            showDateDialog.value = false
        }
    }
}

@Composable
fun CustomDatePickerDialog(
    label: String,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        DatePickerUI(label, onDismissRequest)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerUI(
    label: String,
    onDismissRequest: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
//        elevation = 10.dp,
//        backgroundColor = Color.White,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.h1,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            val chosenYear = remember { mutableStateOf(currentYear) }
            val chosenMonth = remember { mutableStateOf(currentMonth) }
            val chosenDay = remember { mutableStateOf(currentDay) }

            DateSelectionSection(
                onYearChosen = { chosenYear.value = it.toInt() },
                onMonthChosen = { chosenMonth.value = monthsNames.indexOf(it) },
                onDayChosen = { chosenDay.value = it.toInt() },
            )

            Spacer(modifier = Modifier.height(30.dp))

            val context = LocalContext.current
            Button(
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                onClick = {
                    Toast.makeText(context, "${chosenDay.value}-${chosenMonth.value}-${chosenYear.value}", Toast.LENGTH_SHORT).show()
                    onDismissRequest()
                }
            ) {
                Text(
                    text = "Done",
                    style = MaterialTheme.typography.button,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
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
