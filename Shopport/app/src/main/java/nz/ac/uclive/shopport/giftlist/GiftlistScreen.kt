package nz.ac.uclive.shopport.giftlist

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import nz.ac.uclive.shopport.common.ShopportAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftlistScreen(modifier: Modifier, navController: NavHostController) {
    Scaffold(
        topBar = { ShopportAppBar(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                modifier = modifier.size(56.dp),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription =  null,
                )
            }
        }
    ) { innerPaddingModifier ->
        Text("Giftlist screen", modifier = modifier.padding(innerPaddingModifier))
    }
}
