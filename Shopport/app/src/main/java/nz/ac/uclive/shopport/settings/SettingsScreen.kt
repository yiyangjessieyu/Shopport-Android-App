package nz.ac.uclive.shopport.settings

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import nz.ac.uclive.shopport.DateNotificationService
import nz.ac.uclive.shopport.R
import nz.ac.uclive.shopport.common.ShopportAppBar
import nz.ac.uclive.shopport.ui.theme.Typography

val NOTIFICATIONS_KEY = "notifications"
val DARK_MODE_KEY = "darkMode"
val LOCATION_SERVICES_KEY = "locationServices"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(modifier: Modifier) {
    val context = LocalContext.current
    val settingsPreferences = LocalContext.current.applicationContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
    val notifications = settingsPreferences.getBoolean(NOTIFICATIONS_KEY, true)
    val locationServices = settingsPreferences.getBoolean(LOCATION_SERVICES_KEY, true)
    val darkModeSetting = settingsPreferences.getString(DARK_MODE_KEY, context.getString(R.string.system))!!

    Column (modifier = modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {
        Text(text = "Settings", style = Typography.displayMedium, fontWeight = FontWeight.Bold)
        Divider(modifier = Modifier
            .padding(0.dp, 2.dp)
            .alpha(0.25f), color = MaterialTheme.colorScheme.primary)
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 4.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = context.getString(R.string.notifications), modifier = Modifier, style = Typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            var checked by remember { mutableStateOf( notifications) }
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    val editor = settingsPreferences.edit()
                    editor.putBoolean(NOTIFICATIONS_KEY, checked)
                    editor.apply()
                },
                modifier = Modifier)
        }
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 4.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = context.getString(R.string.location_services), modifier = Modifier)
            Spacer(modifier = Modifier.weight(1f))
            var checked by remember { mutableStateOf( locationServices) }
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    val editor = settingsPreferences.edit()
                    editor.putBoolean(LOCATION_SERVICES_KEY, checked)
                    editor.apply()
                },
                modifier = Modifier)
        }
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
            verticalAlignment = Alignment.Top) {
            Text(text = context.getString(R.string.dark_mode), modifier = Modifier.padding(vertical = 7.dp))
            Spacer(modifier = Modifier.weight(1f))

            var expanded by rememberSaveable { mutableStateOf(false)}
            var darkMode by rememberSaveable { mutableStateOf(darkModeSetting)}

            fun setDarkMode(mode: String) {
                darkMode = mode
                val editor = settingsPreferences.edit()
                editor.putString(DARK_MODE_KEY, mode)
                editor.apply()
                val nightMode = when (mode) {
                    context.getString(R.string.light) -> AppCompatDelegate.MODE_NIGHT_NO
                    context.getString(R.string.dark) -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
                AppCompatDelegate.setDefaultNightMode(nightMode)
            }

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
                            setDarkMode(context.getString(R.string.system))
                            expanded = false
                        })
                    DropdownMenuItem(
                        text = { Text(context.getString(R.string.light)) },
                        onClick = {
                            setDarkMode(context.getString(R.string.light))
                            expanded = false
                        })
                    DropdownMenuItem(
                        text = { Text(context.getString(R.string.dark)) },
                        onClick = {
                            setDarkMode(context.getString(R.string.dark))
                            expanded = false
                        })
                }
            }
        }
        Divider(modifier = Modifier
            .padding(0.dp, 2.dp)
            .alpha(0.25f), color = MaterialTheme.colorScheme.primary)
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
            verticalAlignment = Alignment.Top) {
            Text(text = context.getString(R.string.show_notification), modifier = Modifier.padding(vertical = 7.dp))
            Spacer(modifier = Modifier.weight(1f))

            var newNotificationTitle = "This is a test notification."
            var context = LocalContext.current
            val dateNotificationService = DateNotificationService(context)

            Box {
                Button(onClick = {
                    dateNotificationService.showNotification(
                        "Testing",
                        newNotificationTitle
                    )
                }) {
                    Text("test notification")
                }
            }
        }
    }
}
