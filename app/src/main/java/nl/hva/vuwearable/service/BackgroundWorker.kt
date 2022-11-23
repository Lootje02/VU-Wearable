package nl.hva.vuwearable.service

//noinspection SuspiciousImport
import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*


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
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_dialog_alert)
                .setContentTitle("Connection Lost")
                .setContentText("Return to the app and reconnect to the device via Wifi")
                .addAction()
        notificationManager.notify(1, notificationBuilder.build());
    }

}