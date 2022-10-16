package nz.ac.uclive.shopport.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.uclive.shopport.R
import nz.ac.uclive.shopport.ShopportDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopportAppBar() {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier.height(120.dp),
        backgroundColor = MaterialTheme.colorScheme.background,
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
            .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_shopport_logo),
                contentDescription = null
            )
        }
    }
}