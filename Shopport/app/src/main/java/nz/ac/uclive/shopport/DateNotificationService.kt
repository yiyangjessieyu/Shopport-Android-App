package nz.ac.uclive.shopport

import android.icu.util.Calendar
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar.*
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import nz.ac.uclive.shopport.settings.NOTIFICATIONS_KEY
import java.util.*

class DateNotificationService (
    private val context: Context
) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val DATE_CHANNEL_ID = "date_channel"
        const val KEY_ID = "id"
    }

    fun setAllDateNotifications() {
        var alarmMgr: AlarmManager? = null

        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        setDateNotification(
            alarmMgr,
            context = context,
            context.getString(R.string.new_year_date),
            0, 1, 0, 0
        )

        setDateNotification(
            alarmMgr,
            context = context,
            context.getString(R.string.xmas_date),
            11, 25, 9, 0
        )

        setDateNotification(
            alarmMgr,
            context = context,
            context.getString(R.string.matariki_date),
            5, 24, 9, 0
        )

        setDateNotification(
            alarmMgr,
            context = context,
            context.getString(R.string.mother_date),
            4, 14, 9, 0
        )

        setDateNotification(
            alarmMgr,
            context = context,
            context.getString(R.string.father_date),
            8, 3, 9, 0
        )
    }

    fun setDateNotification(
        alarmMgr: AlarmManager,
        context: Context,
        dateType: String,
        month: Int,
        dayOfMonth: Int,
        hour: Int,
        minute: Int,
    ) {

        var intent: PendingIntent = Intent(context, DateNotificationReceiver::class.java).apply {
            action = dateType
        }.let { intent ->
            PendingIntent.getBroadcast(context, 1, intent, 0)
        }

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.MONTH, month) // starts from 0
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val today = Calendar.getInstance(Locale.getDefault())

        if (shouldNotifyToday(calendar, today)) {
            // system should invoke an alarm at a nearly precise time in the future.
            // //The device will wake up and fire a pending intent.
            // Keep in mind that if a device is in a low-power mode, the system won’t invoke an alarm.
            alarmMgr.setRepeating(
                AlarmManager.RTC_WAKEUP, // trigger isn’t time-critical, so use RTC_WAKEUP to set exact alarms.
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY * 365,
                intent
            )
        }

    }

    private fun shouldNotifyToday(calendar: Calendar, today: Calendar): Boolean {
        return calendar.weekData == today.weekData &&
            today.get(java.util.Calendar.HOUR_OF_DAY) <= 24
    }


    fun showNotification(dateType: String, title: String) {

        val settingsPreferences = context.applicationContext.getSharedPreferences(
            context.getString(R.string.lowercase_settings),
            Context.MODE_PRIVATE
        )
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
                .setContentTitle(context.getString(R.string.tomorrow_is) + " " + title)
                .setContentText(context.getString(R.string.start_gifting_for) + " " + dateType)
                .setContentIntent(activityPendingIntent) // clicking on the notification will take you to the app
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(1, notificationBuilder)
        }
    }

    fun showPersonalNotification(action: String) {

        val actionSplit = action.split(context.getString(R.string.split))

        val settingsPreferences = context.applicationContext.getSharedPreferences(
            context.getString(R.string.lowercase_settings),
            Context.MODE_PRIVATE
        )
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
                .setContentTitle(context.getString(R.string.tomorrow_is)  + " " + actionSplit[0])
                .setContentText(context.getString(R.string.start_gifting_for)  + " " +  actionSplit[2] + ". " + actionSplit[1])
                .setContentIntent(activityPendingIntent) // clicking on the notification will take you to the app
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(1, notificationBuilder)
        }
    }
}
