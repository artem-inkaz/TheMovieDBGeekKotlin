package com.example.themoviedbgeekkotlin.map.api

import com.example.themoviedbgeekkotlin.api.LogginInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitModulePlace {

    private const val ROOT_URL = "https://maps.googleapis.com/maps/api/place/"
    private val  logginInterceptor: LogginInterceptor? = null

    private val client =  OkHttpClient.Builder().apply {
//        OkHttpClient().newBuilder()
            addInterceptor(PlacesApiHeaderInterceptor())
           logginInterceptor?.providesHttpLoggingInterceptor()
    }
            .build()


    val retrofit: Retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(ROOT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

//        fun create(): PlaceInterface {
//            val logger = HttpLoggingInterceptor()
//            logger.level = HttpLoggingInterceptor.Level.BODY
//            val okHttpClient = OkHttpClient.Builder()
//                    .addInterceptor(logger)
//                    .build()
//
//            val converterFactory = GsonConverterFactory.create()
//            val retrofitPlace = Retrofit.Builder()
//                    .baseUrl(ROOT_URL)
//                    .client(okHttpClient)
//                    .addConverterFactory(converterFactory)
//                    .build()
//            return retrofit.create(PlaceInterface::class.java)
//        }

}

class PlacesApiHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url

        val request = originalRequest.newBuilder()
                .url(originalHttpUrl)
//                .addHeader(API_KEY_HEADER, BuildConfig.THEMOVIEDB_API_KEY)
                .build()

        return chain.proceed(request)
    }
}