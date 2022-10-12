package nz.ac.uclive.shopport


import android.Manifest.permission.*
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import nz.ac.uclive.shopport.common.LocationViewModel
import nz.ac.uclive.shopport.ui.theme.ShopportTheme


class MainActivity : ComponentActivity() {

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

            // TODO: remove this temp button to show notificaitons
            var newNotificationTitle = "Important date upcoming up, get ready to gift!"
            val dateNotificationService = DateNotificationService(applicationContext) // TODO: use this to customise notifications
            Box(modifier = Modifier.fillMaxSize()) {
                Button(onClick = {
                    dateNotificationService.showNotification(10, newNotificationTitle) // TODO: use this to trigger notification
                }) {
                    Text(text = "Show notification")
                }
            }
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
                NotificationManager.IMPORTANCE_DEFAULT
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
}



@Preview
@Composable
fun AllPreview() {
    Shopport()
}
