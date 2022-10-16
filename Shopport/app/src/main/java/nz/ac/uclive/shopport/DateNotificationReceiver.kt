package nz.ac.uclive.shopport

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DateNotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val dateNotificationService = DateNotificationService(context)

        if (context != null && intent != null && intent.action != null) {

            if (intent.action!!.equals(R.string.demo_date)) {
                dateNotificationService.showNotification(R.string.demo_date.toString(), "Reminding you of this important date")
            }
        }


//        dateNotificationService.showNotification("Testing", "Reminding you of this important date")
    }
}
