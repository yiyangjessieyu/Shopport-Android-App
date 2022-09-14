package nz.ac.uclive.shopport.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(modifier: Modifier) {
    Text(text = "Settings", modifier = modifier)
    Column (modifier = modifier) {
        Row (modifier = modifier) {
            Text(text = "Notifications", modifier = modifier)
            var checked by remember { mutableStateOf( true) }
            Switch(
                checked = checked,
                onCheckedChange = {checked = it},
                modifier = modifier)
        }
    }
}