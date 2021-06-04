package com.example.themoviedbgeekkotlin.backgroundworkmanager

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.*
import com.example.themoviedbgeekkotlin.model.MovieListRepository
import com.example.themoviedbgeekkotlin.model.MovieListRepositoryImpl
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SimpleMovieWorkerManager(private val context: Context, params: WorkerParameters) :
        Worker(context,params) {

    // Репозиторий для работы с таблицей БД
//    private val repository by lazy { GeoInfoDatabase.getDatabase(context).wordDao() }

    override fun doWork(): Result {
//        val repository: MovieListRepository = MovieListRepositoryImpl()
//        repository.getMovieFromServer()
        Log.d("SimpleWorker", "Данные есть!")

        return Result.success()
    }

//    override fun createWork(): Single<Result> {
//        val repository: MovieListRepository = MovieListRepositoryImpl()
//        return repository.getMovieFromLocalStorage()
//            // Для работы с БД необходимо изменить поток на io()
//            .observeOn(Schedulers.io())
//            // После получения данных сохраняем в БД
//            .flatMap {
//                repository.getMovieFromLocalStorage()
//            }
//            // Возвращаем статус success()
//            .map { Result.success() }
//            // Если произойдет ошибка возвращаем статус failure()
//            .onErrorReturn {
//                Log.d("LocationWorker", "Ошибка получения данных")
//                Result.failure()
//            }
//    }



    companion object {
        private const val SIMPLE_WORKER_TAG = "SimpleWorkerTag"

        // 1. Метод для создание условий
        private fun createConstraints() = Constraints.Builder()
            // 1a. Условие подключения к Wi-Fi
            .setRequiredNetworkType(NetworkType.UNMETERED)
            // 1b. Подключения к зарядке
//           .setRequiresCharging(false)
            .build()

        // 1. Метод для создания OneTimeWorkRequest
        private fun createWorkRequest(data: Data): OneTimeWorkRequest {
            return OneTimeWorkRequest.Builder(SimpleMovieWorkerManager::class.java)
// 2. Необходимо задать условия при построении WorkRequest
                .setConstraints(createConstraints())
                .setInputData(data)
         //указываем уникальный тег для идентификации данного WorkRequest для случая отмены
                .addTag(SIMPLE_WORKER_TAG)
                .build()
        }

        // 2. Метод для запуска OneTimeWorkRequest
        fun startWork(context: Context) {
            val work = createWorkRequest(Data.EMPTY)
            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    SIMPLE_WORKER_TAG,
                    ExistingWorkPolicy.APPEND,
                    work
                )
        }

        // 3. Метод для остановки
        fun cancelWork(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(SIMPLE_WORKER_TAG)
        }
    }
}