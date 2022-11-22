import android.R
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import nl.hva.vuwearable.MainActivity


class YourService : Service() {
    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // do your jobs here
        startForeground()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForeground() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, 0
        )
        startForeground(
            NOTIF_ID, NotificationCompat.Builder(
                this,
                NOTIF_CHANNEL_ID
            )
                .setOngoing(true)
                .setSmallIcon(R.drawable.btn_star)
                .setContentTitle(getString(R.string.cancel))
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build()
        )
    }

    companion object {
        private const val NOTIF_ID = 1
        private const val NOTIF_CHANNEL_ID = "channel01"
    }
}