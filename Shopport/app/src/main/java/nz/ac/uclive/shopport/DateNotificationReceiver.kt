package nz.ac.uclive.shopport

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import nz.ac.uclive.shopport.settings.NOTIFICATIONS_KEY

class TestNotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val dateNotificationService = DateNotificationService(context)
        dateNotificationService.showNotification("Testing", "Reminding you of this important e")
    }
}

class XmasNotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val dateNotificationService = DateNotificationService(context)
        dateNotificationService.showNotification("Christmas", "Reminding you of this important e")
    }
}
class MatarikiNotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val dateNotificationService = DateNotificationService(context)
        dateNotificationService.showNotification("Matariki", "Reminding you of this important e")
    }
}
class DemoDayNotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val dateNotificationService = DateNotificationService(context)
        dateNotificationService.showNotification("Demo Day", "Reminding you of this important e")
    }
}
