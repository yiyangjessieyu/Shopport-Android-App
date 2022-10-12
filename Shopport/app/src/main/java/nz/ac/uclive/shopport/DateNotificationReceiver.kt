package nz.ac.uclive.shopport

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DateNotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val dateNotificationService = DateNotificationService(context)
        dateNotificationService.showNotification(20)
    }
}
