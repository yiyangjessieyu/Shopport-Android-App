package nz.ac.uclive.shopport.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import nz.ac.uclive.shopport.R
import nz.ac.uclive.shopport.ui.theme.Typography
import kotlin.math.exp


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(modifier: Modifier) {
    val context = LocalContext.current
    Column (modifier = modifier.padding(horizontal = 8.dp), horizontalAlignment = Alignment.Start) {
        Text(text = "Settings", style = Typography.titleLarge, modifier = Modifier.padding(0.dp))

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = context.getString(R.string.notifications), modifier = Modifier, style = Typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            var checked by remember { mutableStateOf( true) }
            Switch(
                checked = checked,
                onCheckedChange = {checked = it},
                modifier = Modifier)
        }
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = context.getString(R.string.location_services), modifier = Modifier)
            Spacer(modifier = Modifier.weight(1f))
            var checked by remember { mutableStateOf( true) }
            Switch(
                checked = checked,
                onCheckedChange = {checked = it},
                modifier = Modifier)
        }
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
            verticalAlignment = Alignment.Top) {
            Text(text = context.getString(R.string.dark_mode), modifier = Modifier.padding(vertical = 7.dp))
            Spacer(modifier = Modifier.weight(1f))

            var expanded by rememberSaveable { mutableStateOf(false)}
            var darkMode by rememberSaveable { mutableStateOf(context.getString(R.string.system))}

            Box {
                Button(onClick = { expanded = true }) {
                    Text(darkMode)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {

                    DropdownMenuItem(
                        text = { Text(context.getString(R.string.system)) },
                        onClick = {
                            darkMode = context.getString(R.string.system)
                            expanded = false
                        })
                    DropdownMenuItem(
                        text = { Text(context.getString(R.string.light)) },
                        onClick = {
                            darkMode = context.getString(R.string.light)
                            expanded = false
                        })
                    DropdownMenuItem(
                        text = { Text(context.getString(R.string.dark)) },
                        onClick = {
                            darkMode = context.getString(R.string.dark)
                            expanded = false
                        })
                }
            }
        }
    }
}