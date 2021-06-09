package com.example.themoviedbgeekkotlin

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import com.example.themoviedbgeekkotlin.notification.MoviesNotificationHelper

//необходимо зарегистрировать канал уведомлений.
// Для этого создайте класс-наследник от Application и назовите его FitnessApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MoviesNotificationHelper.createNotificationChannel(
                this,
                NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
                getString(R.string.app_name), "Канал THEMOVIEDB - приложения."
        )
    }
}
//Теперь мы сможем отправить уведомление.