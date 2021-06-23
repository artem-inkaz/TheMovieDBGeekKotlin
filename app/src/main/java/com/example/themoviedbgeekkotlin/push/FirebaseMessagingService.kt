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

////    override fun onMessageReceived(remoteMessage: RemoteMessage) {
////        super.onMessageReceived(remoteMessage)
////        remoteMessage.notification?.apply(::sendNotification)
////    }
//
//    override fun onMessageReceived(p0: RemoteMessage) {
////        super.onMessageReceived(remoteMessage)
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: ${p0.from}")
//
//        // Check if message contains a data payload.
//        p0.data?.let {
//            Log.d(TAG, "Message data payload: " + p0.data)
//        }
//
//        // TODO Step 3.6 check messages for notification and call sendNotification
//        // Check if message contains a notification payload.
//        p0.notification?.let {
//            Log.d(TAG, "Message Notification Body: ${it.body}")
//            sendNotification(it.body!!)
//        }
//
//    }
//
//    private fun sendNotification(messageBody: String) {
//        val notificationManager =  ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
//        //                requireContext(), "Супер Уведомление", "Это уведомление для отладки", "", true
//        notificationManager.createMoviesNotificationFCM(applicationContext,"Канал THEMOVIEDB - Любимые фильмы всегда под рукой(Now Playing)", messageBody,"", true)
//    }
//
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//        Log.d(TAG, "Refreshed token: $token")
//    }
//
//    companion object {
//        private const val TAG = "MyFirebaseMsgService"
//    }

//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        val remoteMessageData = remoteMessage.data
//        if (remoteMessageData.isNotEmpty()) {
//            handleDataMessage(remoteMessageData.toMap())
//        }
//    }
//
//    private fun handleDataMessage(data: Map<String, String>) {
//        val title = data[PUSH_KEY_TITLE]
//        val message = data[PUSH_KEY_MESSAGE]
//        if (!title.isNullOrBlank() && !message.isNullOrBlank()) {
//            showNotification(title, message)
//        }
//    }
//
//    private fun showNotification(title: String, message: String) {
//        val notificationBuilder =
//            NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
//                setSmallIcon(R.drawable.ic_movie)
//                setContentTitle(title)
//                setContentText(message)
//                priority = NotificationCompat.PRIORITY_DEFAULT
//            }
//
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createNotificationChannel(notificationManager)
//        }
//        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createNotificationChannel(notificationManager: NotificationManager) {
//        val name = "Channel name"
//        val descriptionText = "Channel description"
//        val importance = NotificationManager.IMPORTANCE_DEFAULT
//        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
//            description = descriptionText
//        }
//        notificationManager.createNotificationChannel(channel)
//    }
//
//    override fun onNewToken(token: String) {
//        //Отправить токен на сервер
//        Log.d(TAG, "Refreshed token: $token")
//    }
//
//    companion object {
//        private const val TAG = "MyFirebaseMsgService"
//        private const val PUSH_KEY_TITLE = "title"
//        private const val PUSH_KEY_MESSAGE = "message"
//        private const val CHANNEL_ID = "channel_id"
//        private const val NOTIFICATION_ID = 37
//    }

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