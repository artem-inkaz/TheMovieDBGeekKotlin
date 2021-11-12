package com.example.themoviedbgeekkotlin.push

import android.util.Log
import android.widget.Toast
import com.example.themoviedbgeekkotlin.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.coroutines.suspendCoroutine

object RepositoryFCM {
        // Функция получение токена
    fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            token
        })
    }
}