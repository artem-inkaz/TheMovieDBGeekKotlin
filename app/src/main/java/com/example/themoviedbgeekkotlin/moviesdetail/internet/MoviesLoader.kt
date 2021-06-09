package com.example.themoviedbgeekkotlin.moviesdetail.internet

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.themoviedbgeekkotlin.BuildConfig
//import androidx.viewbinding.BuildConfig
import com.example.themoviedbgeekkotlin.BuildConfig.THEMOVIEDB_API_KEY
import com.example.themoviedbgeekkotlin.api.MovieDto
import com.example.themoviedbgeekkotlin.api.MoviesDto
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executor
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

@RequiresApi(Build.VERSION_CODES.N)
class MoviesLoader(private val listener: MoviesLoaderListener, private val movieId: Int) {

    //https://api.themoviedb.org/3/movie/632357?api_key=3847095cad8449ec1b9ca6240fa9102c
    //https://api.themoviedb.org/3/movie/popular?api_key=3847095cad8449ec1b9ca6240fa9102c&page=1

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadMovie() {
        try {
            val uri =
                URL("https://api.themoviedb.org/3/movie/${movieId}?api_key=${BuildConfig.THEMOVIEDB_API_KEY}")
//                URL("https://api.themoviedb.org/3/movie/popular?api_key=${BuildConfig.THEMOVIEDB_API_KEY}")
            val handler = Handler(Looper.getMainLooper())
            Thread(Runnable {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
//                    urlConnection.addRequestProperty(
//                        "X-Yandex-API-Key",
//                        BuildConfig.THEMOVIEDB_API_KEY
//                    )
                    urlConnection.readTimeout = 0
                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))

                    // преобразование ответа от сервера (JSON) в модель данных (WeatherDTO)
                    val movieDto: MovieDto =
                        Gson().fromJson(getLines(bufferedReader), MovieDto::class.java)
                    handler.post { listener.onLoaded(movieDto) }
                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()
                    listener.onFailed(e)
                } finally {
                    urlConnection.disconnect()
                }
            }).start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
            listener.onFailed(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    interface MoviesLoaderListener {
        fun onLoaded(movieDto: MovieDto)
        fun onFailed(throwable: Throwable)
    }
}
