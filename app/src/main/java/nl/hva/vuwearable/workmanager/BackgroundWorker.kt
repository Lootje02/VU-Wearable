package nl.hva.vuwearable.workmanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import nl.hva.vuwearable.MainActivity
import nl.hva.vuwearable.R


class BackgroundWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val CHANNEL_ID = "notificationWifi"

    private val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun doWork(): Result {
        createNotificationChannel()

        notifyUser()

        return Result.retry()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                name = "notification channel"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun notifyUser() {
        val activityActionIntent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val activityActionPendingIntent: PendingIntent =
            PendingIntent.getActivity(applicationContext, 0, activityActionIntent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_vu_logo)
                .setContentTitle("Connection Lost")
                .setContentText("Return to the app and reconnect the device")
                .addAction(R.drawable.ic_vu_logo,"Fix issue",activityActionPendingIntent)

        notificationManager.notify(1, notificationBuilder.build())
    }
}