package com.example.cloudmessagingfirebasse.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.cloudmessagingfirebasse.R
import com.example.cloudmessagingfirebasse.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject

class Service : FirebaseMessagingService() {

    private val tokenManager: TokenManager by inject()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = getString(R.string.app_name)
        val message = getString(R.string.quickstart_message)

        if(remoteMessage.data.isNotEmpty()) {
            triggerNotification(title, remoteMessage.data["message"] ?: message)
        } else if (remoteMessage.notification != null) {
            triggerNotification(remoteMessage.notification?.body ?: title, message)
        }
    }

    override fun onNewToken(token: String) {
        tokenManager.updateToken(token)
    }

    private fun triggerNotification(title: String, msg: String) {
        val notificationId = 1
        val channelId = "channel"
        createChannel(channelId, title)
        val notificationManager = NotificationManagerCompat.from(this)
        val it = Intent(this, MainActivity::class.java)
        val pit = PendingIntent.getActivity(this,
            0, it, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(this, channelId)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(msg)
            .setContentIntent(pit)
            .setColor(ContextCompat.getColor(this, R.color.teal_200))
            .setAutoCancel(true)
        notificationManager.notify(notificationId, builder.build())
    }

    private fun createChannel(channelId: String, title: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    title,
                    NotificationManager.IMPORTANCE_DEFAULT)
            )
        }
    }

    companion object {
        private const val TAG = "Service"
    }
}