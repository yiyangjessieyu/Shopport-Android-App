package nz.ac.uclive.shopport


import android.Manifest.permission.*
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import nz.ac.uclive.shopport.settings.DARK_MODE_KEY
import nz.ac.uclive.shopport.settings.LOCATION_SERVICES_KEY
import nz.ac.uclive.shopport.settings.NOTIFICATIONS_KEY
import nz.ac.uclive.shopport.ui.theme.ShopportTheme
import java.util.*


class MainActivity : AppCompatActivity() {

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        createNotificationChannel()

        super.onCreate(savedInstanceState)

        if (!hasPermissions(this, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, CAMERA)) {
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION, ACCESS_FINE_LOCATION, CAMERA), 1);
        }
        StrictMode.setVmPolicy(
            VmPolicy.Builder(StrictMode.getVmPolicy())
                .detectLeakedClosableObjects()
                .build()
        )

        setContent {
            Shopport()
        }

    }

    // youtube.com/watch?v=LP623htmWcI
    private fun createNotificationChannel() {
        val descriptionText = "NotificationDescriptionText"
        val notificationChannelName = "NotificationChannelName"

        // Create the NotificationChannel, but only on API 26+
        // Condition needed because the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                DateNotificationService.DATE_CHANNEL_ID,
                notificationChannelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.description = descriptionText

            // Register the channel
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun Shopport() {

    val context = LocalContext.current
    val settingsPreferences = context.applicationContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
    val darkModeSetting = settingsPreferences.getString(DARK_MODE_KEY, context.getString(R.string.system))!!

    val nightMode = when (darkModeSetting) {
        context.getString(R.string.light) -> AppCompatDelegate.MODE_NIGHT_NO
        context.getString(R.string.dark) -> AppCompatDelegate.MODE_NIGHT_YES
        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
    AppCompatDelegate.setDefaultNightMode(nightMode)

    ShopportTheme {
        val tabs = remember { ShopportScreens.values() }
        val navController = rememberAnimatedNavController()
        Scaffold(
            bottomBar = {
                ShopportBottomBar(navController = navController, tabs)
            }
        ) { innerPaddingModifier ->
            NavigationHost(
                navController = navController,
                modifier = Modifier.padding(innerPaddingModifier)
            )
        }
    }

    val dateNotificationService = DateNotificationService(context)
    dateNotificationService.setAllDateNotifications()
}

@Preview
@Composable
fun AllPreview() {
    Shopport()
}
