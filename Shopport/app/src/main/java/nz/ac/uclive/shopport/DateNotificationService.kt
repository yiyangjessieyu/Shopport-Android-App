package nz.ac.uclive.shopport

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class DateNotificationService (
    private val context: Context
    )
{
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(counter: Int) {
        val activityIntent = Intent(context, MainActivity::class.java)
        // Wrapper around normal intent that allow outside application to execute some code in your app.
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        val incrementIntent = PendingIntent.getBroadcast(
            context,
            2,
            Intent(context, DateNotificationReceiver::class.java), // can use .apply {putExtra("value", 0)} to add data
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        val notification = NotificationCompat.Builder(context, DATE_CHANNEL_ID)
            .setSmallIcon(R.drawable.shopport_logo_only)
            .setContentTitle("Important date upcoming up, get ready to gift!")
            .setContentText("The count is $counter")
            .setContentIntent(activityPendingIntent)
            .addAction(
                R.drawable.ic_gift,
                "Increment",
                incrementIntent
            )
            .build()

        notificationManager.notify(1, notification)
    }


    companion object {
        const val DATE_CHANNEL_ID = "date_channel"
    }
}