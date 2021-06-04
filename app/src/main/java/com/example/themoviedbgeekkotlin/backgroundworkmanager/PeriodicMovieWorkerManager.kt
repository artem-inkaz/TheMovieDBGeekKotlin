package com.example.themoviedbgeekkotlin.backgroundworkmanager

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.androidacademy.data.Database_movies
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.model.MovieListRepository
import com.example.themoviedbgeekkotlin.model.MovieListRepositoryImpl
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class PeriodicMovieWorkerManager(private val context: Context, params: WorkerParameters) :
    Worker(context,params) {

    override fun doWork(): Result {
//        val repository: MovieListRepository = MovieListRepositoryImpl()
//        repository.getMovieFromLocalStorage()
        return Result.success()
    }

//    @SuppressLint("CheckResult")
//    override fun createWork(): Single<Result> {
//        // Репозиторий для получения фильмов
//        val repository: MovieListRepository = MovieListRepositoryImpl()
////        val bdMovies: Movie = Database_movies()
//       return repository.getMovieFromLocalStorage()
//            /* Для работы с БД необходимо изменить поток на io() */
//           .observeOn(Schedulers.io())
//            .flatMap {
//                repository.getMovieFromLocalStorage()
//            }
//            // Возвращаем статус success()
//            .map { Result.success() }
//            // Если произойдет ошибка возвращаем статус failure()
////            .onErrorReturn {
////                Log.d("LocationWorker", "Ошибка получения данных")
////                Result.failure()
////            }
//    }



    companion object {
        private const val PERIODIC_WORKER_TAG = "PeriodicWorkerTag"

        // 1. Метод для создание условий
        private fun createConstraints() = Constraints.Builder()
            // 1a. Условие подключения к Wi-Fi
            .setRequiredNetworkType(NetworkType.UNMETERED)
            // 1b. Подключения к зарядке
            .setRequiresCharging(true) // на эмуляторе может не работать
            .build()

        // Метод для создания PeriodicWorkRequest
        private fun createWorkRequest(data: Data): PeriodicWorkRequest {
            // Заменяем на PeriodicWorkRequest и добавляем интервал
            return PeriodicWorkRequest.Builder(PeriodicMovieWorkerManager::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(createConstraints())
                .setInputData(data)
                .addTag(PERIODIC_WORKER_TAG)
                .build()
        }
        // Метод для запуска PeriodicWorkRequest
        fun startWork(context: Context) {
            val work = createWorkRequest(Data.EMPTY)
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    PERIODIC_WORKER_TAG,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    work
                )
        }

        // 3. Метод для остановки
        fun cancelWork(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(PERIODIC_WORKER_TAG)
        }
    }
}