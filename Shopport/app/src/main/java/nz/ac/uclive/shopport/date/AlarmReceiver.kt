package nz.ac.uclive.shopport.date

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import nz.ac.uclive.shopport.DateNotificationService
import nz.ac.uclive.shopport.R

class AlarmReceiver: BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {

//        if (context != null && intent != null && intent.action != null) {
//            // 1
//            if (intent.action!!.equals(context.getString(R.string.action_notify_administer_medication), ignoreCase = true)) {
//                if (intent.extras != null) {
//                    // 2
//                    val reminderData = DataUtils.getReminderById(intent.extras!!.getInt(ReminderDialog.KEY_ID))
//                    if (reminderData != null) {
//                        // 3
//                        NotificationHelper.createNotificationForPet(context, reminderData)
//                    }
//                }
//            }
//        }

        // TODO: if a condition that date matches, then send notification https://www.raywenderlich.com/1214490-android-notifications-tutorial-getting-started
        val dateNotificationService = DateNotificationService(context)
        dateNotificationService.showNotification(20, "Reminding you of this important e")

    }


}
