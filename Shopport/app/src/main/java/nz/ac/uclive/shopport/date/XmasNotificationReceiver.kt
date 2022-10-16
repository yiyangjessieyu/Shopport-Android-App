package nz.ac.uclive.shopport.date

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import nz.ac.uclive.shopport.DateNotificationService

class XmasNotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val dateNotificationService = DateNotificationService(context)
        dateNotificationService.showNotification("Christmas", "Reminding you of this important date")
    }
}
