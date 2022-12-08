package nl.hva.vuwearable.workmanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import nl.hva.vuwearable.MainActivity
import nl.hva.vuwearable.R
import nl.hva.vuwearable.udp.UDPConnection


class BackgroundWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    private val measurementStarted = true

    private val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    /**
     * Check if sdk allows notifications and create channel if not present
     * Check if measurement has been started and create thread for UDP, if udp is connected true, else false
     * Function that gets executed everytime worker is called
     */
    override fun doWork(): Result {

        if (checkSDK() && notificationManager.getNotificationChannel(
                CHANNEL_ID
            ) == null
        ) {
            createNotificationChannel()
        }

        if (measurementStarted) {
            Thread(
                UDPConnection(
                    this.applicationContext,
                    60,
                    60,
                    setConnectedCallback = { isConnected, isReceivingData ->
                        if (!isConnected || !isReceivingData) {
                            createNotification()
                        }
                    })
            ).start()
        }
        return Result.success()
    }

    private fun checkSDK(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    private fun createNotificationChannel() {
        if (checkSDK()) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                name = "notification channel"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Create intent and pending intent for action in notification builder
     * Create notification that redirects user to dashboard
     */
    private fun createNotification() {
        val activityActionIntent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext, 0, activityActionIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL).setSmallIcon(R.drawable.ic_vu_logo)
                .setContentTitle("Connection Lost")
                .setContentText("Lost connection to the wearable")
                .addAction(R.drawable.ic_vu_logo, "Fix issue", pendingIntent)
        notificationManager.notify(1, notificationBuilder.build())
    }

    companion object {
        private const val CHANNEL_ID = "notificationWifi"
    }
}