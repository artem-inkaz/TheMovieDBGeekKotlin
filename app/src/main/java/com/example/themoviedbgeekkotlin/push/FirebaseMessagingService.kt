package com.example.themoviedbgeekkotlin.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.themoviedbgeekkotlin.MainActivity
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.notification.MoviesNotificationHelper
import com.example.themoviedbgeekkotlin.notification.MoviesNotificationHelper.createMoviesNotificationFCM
import com.example.themoviedbgeekkotlin.notification.createMoviesNotificationFCM
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val TAG = "MyFirebaseMsgService"
    }
//
    private val notificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
//
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID,
                "Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "This is channel description"
            }.also {
                notificationManager.createNotificationChannel(it)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TAG", "Refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

       message.notification?.apply(::showNotification)
    }

    private fun showNotification(notification: RemoteMessage.Notification) {

        val resultIntent = Intent(this, MainActivity::class.java)
            .apply {
                putExtra("ARG_TITLE", notification.title)
            }

        val pendingIntent = PendingIntent.getActivities(
            this,
            0,
            arrayOf(resultIntent),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle(notification.title)
            setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(notification.body)
            )
            setSmallIcon(R.drawable.ic_movie)
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }.also {
            notificationManager.notify(1, it.build())
        }
    }
}