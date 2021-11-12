package com.example.themoviedbgeekkotlin.api

import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

class LogginInterceptor {

    fun providesHttpLoggingInterceptor() : Interceptor? = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}