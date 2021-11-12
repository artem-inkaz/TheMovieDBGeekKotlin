/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.themoviedbgeekkotlin.map.geofence

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.themoviedbgeekkotlin.EXTRA_GEOFENCE_INDEX
import com.example.themoviedbgeekkotlin.LANDMARK_DATA
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.map.MapsMovieFragment

/*
 * We need to create a NotificationChannel associated with our CHANNEL_ID before sending a
 * notification.
 */
fun createChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.channel_name),

                NotificationManager.IMPORTANCE_HIGH
        )
                .apply {
                    setShowBadge(false)
                }

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = context.getString(R.string.notification_channel_description)

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}

/*
 * A Kotlin extension function for AndroidX's NotificationCompat that sends our Geofence
 * entered notification.  It sends a custom notification based on the name string associated
 * with the LANDMARK_DATA from GeofencingConstatns in the GeofenceUtils file.
 */
fun NotificationManager.sendGeofenceEnteredNotification(context: Context, foundIndex: Int) {
    val contentIntent = Intent(context, MapsMovieFragment::class.java)
    contentIntent.putExtra(EXTRA_GEOFENCE_INDEX, foundIndex)
    val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )
    val mapImage = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.ic_movie
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(mapImage)
            .bigLargeIcon(null)

    // Мы используем идентификатор ресурса имени из LANDMARK_DATA вместе с content_text для создания
    // настраиваемое сообщение при срабатывании геозоны.
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.content_text,
                    LANDMARK_DATA[foundIndex].name))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentPendingIntent)
            .setSmallIcon(R.drawable.ic_movie)
            .setStyle(bigPicStyle)
            .setLargeIcon(mapImage)

    notify(NOTIFICATION_ID, builder.build())
}

private const val NOTIFICATION_ID = 33
private const val CHANNEL_ID = "GeofenceChannel"


