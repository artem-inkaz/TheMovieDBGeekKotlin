package com.example.themoviedbgeekkotlin.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

//Чтобы иметь возможность отобразить уведомление когда AlarmManager оповестит систему о том,
// что сработало условие (например подошло время напоминания и дата) нам необходимо зарегистрировать
// BroadcastReceiver. BroadcastReceiver примет отложенный intent,
// в котором будет храниться информация об идентификаторе объекта ReminderData и
// по нему мы сможем позже отобразить всю нужную информацию пользователю в уведомлении.
//
//Для регистрации BroadcastReceiver добавьте в AndroidManifest.xml!
class MoviesAlarmReceiver : BroadcastReceiver() {

    private val TAG = MoviesAlarmReceiver::class.java.simpleName
//Когда сработает AlarmManager – BroadcastReceiver получит intent и дальше мы уже можем использовать
// его как хотим.
// В данном случае получим параметр id и по нему запросим данные из репозитория с базой данных.
// А после этого отобразим уведомление

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive() called with: context = [$context], intent = [$intent]")
        if (context != null && intent != null) {
            if (intent.extras != null) {

                }
            }
        }
    }