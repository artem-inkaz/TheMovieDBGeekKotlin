package com.example.themoviedbgeekkotlin.api

import com.example.themoviedbgeekkotlin.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitModule {

    private val  logginInterceptor: LogginInterceptor? = null

    private val okHttpClient: OkHttpClient? = null

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(MoviesApiHeaderInterceptor())
        logginInterceptor?.providesHttpLoggingInterceptor()
    }
                .build()

    val retrofit: Retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}