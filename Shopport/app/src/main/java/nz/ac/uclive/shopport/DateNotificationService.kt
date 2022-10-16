package nz.ac.uclive.shopport

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar.*
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import nz.ac.uclive.shopport.date.DateItem
import nz.ac.uclive.shopport.settings.NOTIFICATIONS_KEY
import java.text.SimpleDateFormat
import java.util.*

class DateNotificationService (
    private val context: Context
) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val DATE_CHANNEL_ID = "date_channel"
        const val KEY_ID = "id"
    }

    private fun createPendingIntent(context: Context, dateItem: DateItem, day: String?): PendingIntent? {

        // Create the Intent with a destination of DateNotificationReceiver
        val intent = Intent(context.applicationContext, DateNotificationReceiver::class.java).apply {

            action = dateItem.name
            type = "$day-${dateItem.name}" // Set the type – which has to be unique
            // If this is not unique it will overwrite any other PendingIntent with this same type.

            putExtra(KEY_ID, dateItem.id) // Add the dateItem’s ID in the Intent‘s bundle so you can use it in the DateNotificationReceiver.
        }

        //  creating the Intent with a BroadcastReceiver as a target
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun scheduleAlarm(dateItem: DateItem, dayOfWeek: Int, alarmIntent: PendingIntent?, alarmMgr: AlarmManager) {
        // Set up a Calendar for the alarm’s time using the dateItem.
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(MONTH, dateItem.month)
        calendar.set(DAY_OF_MONTH, dateItem.day_of_month)
        calendar.set(HOUR_OF_DAY, dateItem.hour)
        calendar.set(MINUTE, dateItem.minute)
        calendar.set(SECOND, 0)
        calendar.set(MILLISECOND, 0)
        calendar.set(DAY_OF_WEEK, dayOfWeek)

        // Checked whether that alarm should be scheduled today and scheduled it if so.
        val today = Calendar.getInstance(Locale.getDefault())
        if (shouldNotifyToday(dayOfWeek, today, calendar)) { //TODO: proper check of when it should notify
            alarmMgr.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                (1000 * 60 * 60 * 24 * 7).toLong(),
                alarmIntent
            )
            return
        }

        // Else, schedule the alarm to repeat every week at that time.
        calendar.roll(WEEK_OF_YEAR, 1)
        alarmMgr.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            (1000 * 60 * 60 * 24 * 7).toLong(),
            alarmIntent
        )
    }

    fun showNotification(dateType: String, title: String) {

        val settingsPreferences = context.applicationContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val notificationPreferences = settingsPreferences.getBoolean(NOTIFICATIONS_KEY, true)

        if (notificationPreferences) {
            val activityIntent = Intent(context, MainActivity::class.java)

            // Wrapper around normal intent that allow outside application to execute some code in your app.
            val activityPendingIntent = PendingIntent.getActivity(
                context,
                1,
                activityIntent,
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
            )

            val notificationBuilder = NotificationCompat.Builder(context, DATE_CHANNEL_ID)
                .setSmallIcon(R.drawable.shopport_logo_only)
                .setContentTitle(title)
                .setContentText("Get ready to start gifting for $dateType")
                .setContentIntent(activityPendingIntent) // clicking on the notification will take you to the app
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(1, notificationBuilder)
        }
    }
}
