package com.example.themoviedbgeekkotlin.map.api

//import com.example.themoviedbgeekkotlin.api.LogginInterceptor
import com.example.themoviedbgeekkotlin.api.LogginInterceptor
//import com.example.themoviedbgeekkotlin.api.LoginInterceptor
//import com.example.themoviedbgeekkotlin.domain.LoginInterceptor
import com.example.themoviedbgeekkotlin.map.responce.NearbyPlacesResponse
import com.google.android.gms.maps.model.LatLng
import okhttp3.Interceptor
import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface PlaceInterface {

    /**
     * https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&rankby=distance&type=restaurant&key=YOUR_API_KEY
     */

    @GET("nearbysearch/json")
    suspend fun nearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radiusInMeters: Int,
        @Query("type") placeType: String,
        @Query("key") apiKey: String
    ): NearbyPlacesResponse

    @GET
    suspend fun getNearByPlaces(@Url url: String): NearbyPlacesResponse

    @GET
    fun getNearByPlacesCall(@Url url: String): Call<NearbyPlacesResponse>

    companion object {
        private const val ROOT_URL = "https://maps.googleapis.com/maps/api/place/"

        fun create(): PlaceInterface {
            val  logginInterceptor: LogginInterceptor? = null

//            val logger = HttpLoggingInterceptor()
//            logger.level = HttpLoggingInterceptor.Level.BODY
//            val logger = loginInterceptor?.providesHttpLoggingInterceptor()
            val okHttpClient = OkHttpClient.Builder().apply {
                logginInterceptor?.providesHttpLoggingInterceptor()
            }
                    .build()



            val converterFactory = GsonConverterFactory.create()
            val retrofit = Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .client(okHttpClient)
                .addConverterFactory(converterFactory)
                .build()
            return retrofit.create(PlaceInterface::class.java)
        }
    }
}