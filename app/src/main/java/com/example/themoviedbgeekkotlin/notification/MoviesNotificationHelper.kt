package com.example.themoviedbgeekkotlin.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.themoviedbgeekkotlin.MainActivity
import com.example.themoviedbgeekkotlin.R

//Чтобы создать и показать уведомление пользователю, необходимо выполнить следующие шаги:
//
//Создать Notification channel.
//Зарегистрировать его
//Создать Notification
//Отправить уведомление, используя NotificationManager.
//Создание канала уведомлений
//Каналы для уведомлений необходимы для группирования уведомлений похожего типа.
// Начиная с 26 API для уведомлений обязательно определить канал,
// в противном случае они не будут показаны на новых версиях операционной системы Android.
object MoviesNotificationHelper {
    /**
     * Sets up the notification channels for API 26+.
     * Note: This uses package name + channel name to create unique channelId's.
     *
     * @param context     application context
     * @param importance  importance level for the notificaiton channel
     * @param showBadge   whether the channel should have a notification badge
     * @param name        name for the notification channel
     * @param description description for the notification channel
     */

//В начале стоит проверка Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
// Эта проверка необходима, так как NotificationChannelпоявился только в 26 API
// операционной системы Android.
//Необходимо создать уникальное имя для канала уведомлений.
// Имя и описание канала впоследствии будет отображаться в настройках уведомлений вашего приложения.
//Используя метод createNotificationChannelсоздаём канал уведомлений

    fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean,
                                  name: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)
            // Register the channel with the system
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Helps issue the default application channels (package name + app name) notifications.
     * Note: this shows the use of [NotificationCompat.BigTextStyle] for expanded notifications.
     *
     * @param context    current application context
     * @param title      title for the notification
     * @param message    content text for the notification when it's not expanded
     * @param bigText    long form text for the expanded notification
     * @param autoCancel `true` or `false` for auto cancelling a notification.
     * if this is true, a [PendingIntent] is attached to the notification to
     * open the application.
     */

// Создание уведомлений
//1. Создаём уникальный  channelId для этого приложения, используя  имя package name и приложения.
//2.Для создания уведомления используем NotificationCompat.Builder
//3.Единственным обязательным параметром при создании уведомления является иконка.
// 4.С помощью метода setSmallIcon задаём иконку для уведомления.
//5.Задаём заголовок для уведомления
//6.Задаём описание
//7/Указываем стиль NotificationCompat.BigTextStyle().
//Указываем приоритет уведомления. Существуют следующие типы уведомлений:
//PRIORITY_MIN
//PRIORTY_MAX
//PRIORITY_LOW
//PRIORTY_HIGH
//PRIORITY_DEFAULT
//
//8. Устанавливаем уведомление как автоматически скрываемое после того, как пользователь нажмёт на уведомление.
//
//9. После этого мы добавим PendingIntent для открытия приложения.
    fun createMoviesNotification(
        context: Context, title: String, message: String,
        bigText: String, autoCancel: Boolean
    ) {
        // 1
        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"
        // 2
        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_movie) // 3
            setContentTitle(title) // 4
            setContentText(message) // 5
            setAutoCancel(autoCancel) // 6
            setStyle(NotificationCompat.BigTextStyle().bigText(bigText)) // 7
            priority = NotificationCompat.PRIORITY_DEFAULT // 8

            // Создаём Intent и PendingIntent для открытия главного экрана
//1.Создаём  Intent для запуска главного экрана, то есть MainActivity.
//2.Устанавливаем флаги для режима launchMode в котором будет запущено приложение.
//3.Оборачиваем Intent в PendingIntent, используя метод getActivity() для получения Activity которую необходимо запустить.
//4.Вызываем метод setContentIntent() для передачи созданного PendingIntent в NotificationCompat.
// Builder чтобы вызвать его при нажатии на уведомление пользователем.
            val intent = Intent(context, MainActivity::class.java) //1
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //2
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0) //9 //3
            setContentIntent(pendingIntent) //4
        }

        //Отображение уведомления
        //Теперь осталось добавить код, который покажет уведомление.
// Тут мы получаем ссылку на NotificationManagerCompat и
// вызываем метод notify() для отображения уведомления, где 1001 – это просто некий id,
// который является обязательным, а notificationBuilder.build() – создаёт в итоге уведомление,
// которое мы так тщательно конструировали.
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1001, notificationBuilder.build())
    }
}