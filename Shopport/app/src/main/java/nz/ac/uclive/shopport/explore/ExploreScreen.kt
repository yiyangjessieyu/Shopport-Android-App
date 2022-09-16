package nz.ac.uclive.shopport.explore

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import nz.ac.uclive.shopport.R
import nz.ac.uclive.shopport.common.LocationViewModel
import nz.ac.uclive.shopport.common.ShopportAppBar
import nz.ac.uclive.shopport.ui.theme.Typography
import nz.ac.uclive.shopport.ui.theme.md_theme_light_green
import nz.ac.uclive.shopport.ui.theme.md_theme_light_red
import kotlin.math.round


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(modifier: Modifier, navController: NavHostController, shopVm: ShopViewModel, locationVm: LocationViewModel) {
    val context = LocalContext.current
    val location by locationVm.getLocationLiveData().observeAsState()
    if (location == null) {
        Text(context.getString(R.string.enableLocationMessage))
    } else {
        Scaffold(
            topBar = { ShopportAppBar(navController = navController) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { shopVm.getShopList(location!!) },
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
                shopVm.getShopList(location!!)
            })
            ShopView(shopVm, modifier = modifier.padding(it))
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShopView(vm: ShopViewModel, modifier: Modifier) {

    if (vm.errorMessage.isEmpty()) {
        Column(modifier = modifier.padding(16.dp)) {
            Text(text = "Nearby Shops", style = Typography.displayMedium, fontWeight = FontWeight.Bold)
            Divider(modifier = Modifier
                .padding(0.dp, 2.dp)
                .alpha(0.25f), color = MaterialTheme.colorScheme.primary)
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                if (vm.shopList.isEmpty()) {
                    item {
                        Column (modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    items(vm.shopList) { shop ->
                        ShopView(shop = shop)
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