package com.example.themoviedbgeekkotlin.backgroundworkmanager

import android.content.Context
import android.util.Log
import androidx.work.*


class SimpleMovieWorkerManager(private val context: Context, params: WorkerParameters) :
    Worker(context, params) {

    override fun doWork(): Result {
        Log.d("SimpleWorker", "Данные есть!")
        return Result.success()
    }

//    override fun createWork(): Single<Result> {
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