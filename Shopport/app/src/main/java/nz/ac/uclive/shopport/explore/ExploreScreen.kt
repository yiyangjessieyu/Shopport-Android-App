package nz.ac.uclive.shopport.explore

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.twotone.LocationOn
import androidx.compose.material.icons.twotone.Map
import androidx.compose.material.icons.twotone.ViewList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import nz.ac.uclive.shopport.R
import nz.ac.uclive.shopport.common.ShopportAppBar
import nz.ac.uclive.shopport.common.location.LocationDetails
import nz.ac.uclive.shopport.ui.theme.Typography
import nz.ac.uclive.shopport.ui.theme.md_theme_light_green
import nz.ac.uclive.shopport.ui.theme.md_theme_light_red
import kotlin.math.round
import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalConfiguration
import nz.ac.uclive.shopport.settings.LOCATION_SERVICES_KEY


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    modifier: Modifier,
    navController: NavHostController,
    shopVm: ShopViewModel,
    location: LocationDetails?
) {
    val context = LocalContext.current
    val settingsPreferences = LocalContext.current.applicationContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
    val locationServices = settingsPreferences.getBoolean(LOCATION_SERVICES_KEY, true)
    if (location == null || !locationServices) {
        Scaffold(
            topBar = {
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    ShopportAppBar()
                }
            },
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(35.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(context.getString(R.string.enableLocationMessage), textAlign = TextAlign.Center,  fontWeight = FontWeight.Bold, fontSize = 32.sp, lineHeight = 50.sp)
            }
        }
    } else {
        Scaffold(
            topBar = {
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    ShopportAppBar()
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { shopVm.getShopList(location) },
                    modifier = modifier.size(56.dp),
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                    )
                }
            }
        ) {
            LaunchedEffect(Unit, block = {
                shopVm.getShopList(location)
            })
            ShopView(shopVm, modifier = modifier.padding(it), location = location)
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShopView(vm: ShopViewModel, modifier: Modifier, location: LocationDetails?) {

    var isListView by rememberSaveable { mutableStateOf(true) }

    if (vm.errorMessage.isEmpty()) {
        Column(modifier = modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "Nearby Shops",
                    style = Typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { isListView = !isListView }) {
                    Icon(
                        imageVector = if (isListView) Icons.TwoTone.Map else Icons.TwoTone.ViewList,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Divider(modifier = Modifier
                .padding(0.dp, 2.dp)
                .alpha(0.25f), color = MaterialTheme.colorScheme.primary)

            if (isListView) {
                LazyColumn(modifier = Modifier.fillMaxHeight()) {

                    if (vm.shopList.isEmpty()) {
                        item {
                            Column (
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(50.dp))
                            }
                        }
                    } else {
                        items(vm.shopList) { shop ->
                            ShopView(shop = shop)
                        }
                    }
                }
            } else {
                val currentLocation = LatLng(location?.latitude?.toDouble() ?: 43.5320, location?.longitude?.toDouble() ?: 172.6306)
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(currentLocation, 13f)
                }
                GoogleMap(
                    modifier = Modifier.height(350.dp),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(
                        myLocationButtonEnabled = true,
                        zoomControlsEnabled = false,
                        zoomGesturesEnabled = true,
                        rotationGesturesEnabled = true,
                    ),
                    properties = MapProperties(
                        mapType = MapType.NORMAL,
                        isMyLocationEnabled = true,
                    ),
                ) {
                    vm.shopList.forEach { shop ->
                        val shopLocation = LatLng(shop.geometry.location.lat.toDouble(), shop.geometry.location.lng.toDouble())
                        Marker(
                            state = MarkerState(shopLocation),
                            title = shop.name,
                            snippet = "${shop.vicinity.slice(1..20)} - " +
                                    "${shop.openingHours?.openNow?.let { 
                                        if (it) "Open" else "Closed" 
                                    }}",
                        )
                    }
                }
            }
        }
    } else {
        Text(vm.errorMessage)
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopView(shop: Shop) {
    Card (
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row (modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.Top) {
            Column {
                Text(text = shop.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val openText = if (shop.openingHours != null && shop.openingHours.openNow) "Open" else "Closed"
                val openTextColour = if (shop.openingHours != null && shop.openingHours.openNow) md_theme_light_green else md_theme_light_red
                Text(text = openText,
                    style = MaterialTheme.typography.titleSmall,
                    color = openTextColour,
                    modifier = Modifier.alpha(0.75f))
            }
        }
        Row (modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically) {
            for (i in 1..round(shop.rating).toInt()) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            val uri: Uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=qwerty&query_place_id=" + shop.placeId)
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            mapIntent.setPackage("com.google.android.apps.maps");
            val context = LocalContext.current
            IconButton(onClick = { startActivity(context, mapIntent, null) }) {
                Icon(imageVector = Icons.TwoTone.LocationOn, contentDescription = null)
            }
        }
    }
}