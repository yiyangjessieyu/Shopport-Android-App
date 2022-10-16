package nz.ac.uclive.shopport

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import nz.ac.uclive.shopport.settings.NOTIFICATIONS_KEY

class DateNotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val dateNotificationService = DateNotificationService(context)
        dateNotificationService.showNotification("Testing", "Reminding you of this important date")
    }
}

//class XmasNotificationReceiver: BroadcastReceiver() {
//
//    override fun onReceive(context: Context, intent: Intent?) {
//        val dateNotificationService = DateNotificationService(context)
//        dateNotificationService.showNotification("Christmas", "Reminding you of this important date")
//    }
//}
//class MatarikiNotificationReceiver: BroadcastReceiver() {
//
//    override fun onReceive(context: Context, intent: Intent?) {
//        val dateNotificationService = DateNotificationService(context)
//        dateNotificationService.showNotification("Matariki", "Reminding you of this important date")
//    }
//}
//class DemoDayNotificationReceiver: BroadcastReceiver() {
//
//    override fun onReceive(context: Context, intent: Intent?) {
//        val dateNotificationService = DateNotificationService(context)
//        dateNotificationService.showNotification("Demo Day", "Reminding you of this important date")
//    }
//}
