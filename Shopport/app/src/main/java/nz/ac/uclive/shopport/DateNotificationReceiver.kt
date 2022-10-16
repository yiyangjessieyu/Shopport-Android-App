package nz.ac.uclive.shopport

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.common.util.DataUtils
import nz.ac.uclive.shopport.settings.NOTIFICATIONS_KEY

class DateNotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        if (context != null && intent != null && intent.action != null) {

            if (intent.action!!.equals("demo_day", ignoreCase = true)) {

                dateItem = new DateItem(id = 1, "demo_day", 9; 16, 14, 26)


                if (intent.extras != null) {
                    // Looked up the dateItem in the database using the extra id from the Intent’s Bundle.
                    val dateItemData = DataUtils.getReminderById(intent.extras!!.getInt(DateNotificationService.KEY_ID))
                    if (reminderData != null) {
                        // 3
                        NotificationHelper.createNotificationForPet(context, reminderData)
                    }
                }
            }
        }



        val dateNotificationService = DateNotificationService(context)
        dateNotificationService.showNotification("Testing", "Reminding you of this important date")
    }
}
