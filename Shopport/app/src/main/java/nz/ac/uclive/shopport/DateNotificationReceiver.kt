package nz.ac.uclive.shopport

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class DateNotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val dateNotificationService = DateNotificationService(context)

        if (context != null && intent != null && intent.action != null) {

            Log.d("foo", "intent.action.toString() " + intent.action.toString())

            when (intent.action.toString()) {

                context.getString(R.string.testing_notifications) ->
                    dateNotificationService.showNotification(
                        context.getString(R.string.testing_notifications),
                        "Testing" + context.getString(R.string.testing_notifications)
                    )

                context.getString(R.string.testing_notifications_2) ->
                    dateNotificationService.showNotification(
                        context.getString(R.string.testing_notifications_2),
                        "Testing " + context.getString(R.string.testing_notifications_2)
                    )

                context.getString(R.string.demo_date) ->
                    dateNotificationService.showNotification(
                        context.getString(R.string.demo_date),
                        "Good luck it is " + context.getString(R.string.demo_date)
                    )

                context.getString(R.string.xmas_date) ->
                    dateNotificationService.showNotification(
                        context.getString(R.string.xmas_date),
                        "HO HO HO it is " + context.getString(R.string.xmas_date)
                    )

                context.getString(R.string.matariki_date) ->
                    dateNotificationService.showNotification(context.getString(R.string.matariki_date),
                        "Ngā mihi o Matariki, te tau hou Māori"
                    )
            }
        }


//        dateNotificationService.showNotification("Testing", "Reminding you of this important date")
    }
}
