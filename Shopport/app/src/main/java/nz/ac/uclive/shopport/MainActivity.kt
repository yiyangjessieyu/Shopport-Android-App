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

            val dateNotificationService = DateNotificationService(applicationContext)
            Box(modifier = Modifier.fillMaxSize()) {
                Button(onClick = {
                    dateNotificationService.showNotification(10)
                }) {
                    Text(text = "Show notification")
                }
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+
        // This is because the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val descriptionText = "NotificationDescriptionText"

            val channel = NotificationChannel(
                DateNotificationService.DATE_CHANNEL_ID,
                "Date",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = descriptionText

            // Register the channel
//            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)

        val notificationManager2 = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager2.createNotificationChannel(channel)

//        val name = "NotificationChannelName"
//
//        val important = NotificationManager.IMPORTANCE_DEFAULT
//        val channel = NotificationChannel("CHANNEL_ID", name, important).apply {
//            description = descriptionText
//        }
//
//        // tutorial here: https://www.youtube.com/watch?v=bnMncU3uw_o
            // youtube.com/watch?v=LP623htmWcI
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
