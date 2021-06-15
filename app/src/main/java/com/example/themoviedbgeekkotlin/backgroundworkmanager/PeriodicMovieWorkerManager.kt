package com.example.themoviedbgeekkotlin.backgroundworkmanager

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class PeriodicMovieWorkerManager(private val context: Context, params: WorkerParameters) :
        Worker(context, params) {

    override fun doWork(): Result {
        return Result.success()
    }

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
            return PeriodicWorkRequest.Builder(
                    PeriodicMovieWorkerManager::class.java,
                    15,
                    TimeUnit.MINUTES
            )
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