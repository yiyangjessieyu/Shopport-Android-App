package nz.ac.uclive.shopport.date

import android.app.AlarmManager
import android.content.Context
import android.util.Log
import nz.ac.uclive.shopport.giftlist.AddGiftlistItemTopBar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import nz.ac.uclive.shopport.DateNotificationService
import nz.ac.uclive.shopport.R
import nz.ac.uclive.shopport.ShopportDestinations
import nz.ac.uclive.shopport.database.DateItem
import nz.ac.uclive.shopport.database.GiftlistViewModel
import java.util.*
import kotlin.reflect.KFunction4

var dayLiveData = MutableLiveData<Int?>(null)
var monthLiveData = MutableLiveData<Int?>(null)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddDateItem(
    modifier: Modifier,
    navController: NavHostController,
    giftlistViewModel: GiftlistViewModel
) {
    val context = LocalContext.current
    var valid by rememberSaveable { mutableStateOf(false) }

    var alarmMgr: AlarmManager? = null
    alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val dateNotificationService = DateNotificationService(context)

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
    ) {
        newDateItem.title = title
        newDateItem.description = description
        newDateItem.forPerson = forPerson
        newDateItem.forPersonColor = forPersonColor


        valid = title.isNotBlank() && forPerson.isNotBlank()
    }


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
                        dateNotificationService.setDateNotification(
                            alarmMgr,
                            context = context,
                            newDateItem.title + "%SPLIT%" +  newDateItem.description + "%SPLIT%" + newDateItem.forPerson,
                            monthLiveData.value!!, dayLiveData.value!!, 14, 26
                        )
                        Log.e("foo", "month" + monthLiveData.value.toString())
                        Log.e("foo", "day"+ dayLiveData.value.toString())
                        navController.navigate(ShopportDestinations.ADD_DATE_ROUTE) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                        dayLiveData.value = null
                        monthLiveData.value = null
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
    setNewDateItem: KFunction4<String, String, String, Int, Unit>,
    giftlistViewModel: GiftlistViewModel,
) {


    val dayDone: Int? by dayLiveData.observeAsState()
    val monthDone: Int? by monthLiveData.observeAsState()
    val allPersons = giftlistViewModel.getAllForPersons().observeAsState(listOf()).value
    val rnd = Random()


    var titleText by rememberSaveable { mutableStateOf("") }
    var descriptionText by rememberSaveable { mutableStateOf("") }
    var forPersonText by rememberSaveable { mutableStateOf("") }
    var forPersonColor = rememberSaveable { mutableStateOf(Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)).toArgb() ) }

    var colorARGB = giftlistViewModel.getColorForPerson(forPersonText).observeAsState(Color.White.toArgb())

    var isMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var showAddPerson by rememberSaveable { mutableStateOf(false) }
    var showColorPickerDialog = rememberSaveable { mutableStateOf(false) }
    var showDateDialog = rememberSaveable { mutableStateOf(false) }
    var dateSelectionDone = rememberSaveable { mutableStateOf(false) }

    fun updateDateItem() {
        setNewDateItem(
            titleText,
            descriptionText,
            forPersonText,
            forPersonColor.value,
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title field
        item {
            OutlinedTextField(
                value = titleText,
                label = {
                    Text(text = stringResource(R.string.title) + "*")
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
        // Descriptions field
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

        // Person field
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

        // Date field
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
                    }
                ) {

                    Icon(Icons.TwoTone.Event, contentDescription = null)
                    Text(
                        fontSize = 18.sp, modifier = Modifier.padding(start = 8.dp),
                        text = if (dayDone == null && monthDone == null) stringResource(R.string.date) else "${dayDone}-${monthDone?.plus(
                            1
                        )}"
                    )
                }
            }
        }

    }
    if (showColorPickerDialog.value) {
        ColorPickerDialog(showColorPickerDialog, forPersonText, forPersonColor, ::updateDateItem)
    }

    if (showDateDialog.value) {
        CustomDatePickerDialog(dateSelectionDone) {
            showDateDialog.value = false
            updateDateItem()
        }
    }
}

@Composable
fun CustomDatePickerDialog(
    dateSelectionDone: MutableState<Boolean>,
    onDismissRequest: () -> Unit
    ) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            DatePickerUI("Yearly Important Date", dateSelectionDone, onDismissRequest)
        }
    }

    val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

    val days = (1..31).map { it.toString() }
    val monthsNames = listOf(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec"
    )

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DatePickerUI(
        label: String,
        dateSelectionDone: MutableState<Boolean>,
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
    //                style = MaterialTheme.typography.h1,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                val chosenMonth = remember { mutableStateOf(currentMonth) }
                val chosenDay = remember { mutableStateOf(currentDay) }

                DateSelectionSection(
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
                        dayLiveData.value = chosenDay.value
                        monthLiveData.value = chosenMonth.value
                        dateSelectionDone.value = true
                        onDismissRequest()
                    }
                ) {
                    Text(
                        text = "Done",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
}


@Composable
fun DateSelectionSection(
    onMonthChosen: (String) -> Unit,
    onDayChosen: (String) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        InfiniteItemsPicker(
            items = days,
            firstIndex = Int.MAX_VALUE / 2 + (currentDay - 2),
            onItemSelected =  onDayChosen
        )

        InfiniteItemsPicker(
            items = monthsNames,
            firstIndex = Int.MAX_VALUE / 2 - 4 + currentMonth,
            onItemSelected =  onMonthChosen
        )
    }
}

@Composable
fun InfiniteItemsPicker(
    modifier: Modifier = Modifier,
    items: List<String>,
    firstIndex: Int,
    onItemSelected: (String) -> Unit,
) {

    val listState = rememberLazyListState(firstIndex)
    val currentValue = remember { mutableStateOf("") }

    LaunchedEffect(key1 = !listState.isScrollInProgress) {
        onItemSelected(currentValue.value)
        listState.animateScrollToItem(index = listState.firstVisibleItemIndex)
    }

    Box(modifier = Modifier.height(106.dp)) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
            content = {
                items(count = Int.MAX_VALUE, itemContent = {
                    val index = it % items.size
                    if (it == listState.firstVisibleItemIndex + 1) {
                        currentValue.value = items[index]
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = items[index],
                        modifier = Modifier.alpha(if (it == listState.firstVisibleItemIndex + 1) 1f else 0.3f),
//                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                })
            }
        )
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
