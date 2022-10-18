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
import nz.ac.uclive.shopport.database.DateItem
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
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(1, notificationBuilder)
        }
    }
}
