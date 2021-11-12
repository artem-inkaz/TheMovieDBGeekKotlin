package com.example.themoviedbgeekkotlin

import android.app.Application
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.example.themoviedbgeekkotlin.notification.MoviesNotificationHelper
import com.example.themoviedbgeekkotlin.ratings.repository.MovieSearchRepository
import com.example.themoviedbgeekkotlin.repository.MoviesRepositoryImpl


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // для БД
        context = applicationContext

 //необходимо зарегистрировать канал уведомлений.
// Для этого создайте класс-наследник от Application и назовите его FitnessApp
        MoviesNotificationHelper.createNotificationChannel(
                this,
                NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
                getString(R.string.app_name), "Канал THEMOVIEDB - приложения."
        )
        //Теперь мы сможем отправить уведомление.
    }

    companion object {
        // для БД
        private var context: Context? = null
        fun context(): Context = context ?: throw IllegalStateException()

        private val repository by lazy { MoviesRepositoryImpl() }
        fun repository(): MoviesRepositoryImpl = repository

//        private val repositorySearch by lazy { MovieSearchRepository() }
//        fun repositorySearch(): MovieSearchRepository = repositorySearch
    }
}
