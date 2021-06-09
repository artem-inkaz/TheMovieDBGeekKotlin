package com.example.themoviedbgeekkotlin.moviesdetail.service

import android.app.IntentService
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.themoviedbgeekkotlin.BuildConfig
import com.example.themoviedbgeekkotlin.api.MovieDto
import com.example.themoviedbgeekkotlin.api.MovieDtoEx
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val MOVIEID_EXTRA = "MovieId"
private const val REQUEST_GET = "GET"
private const val REQUEST_TIMEOUT = 10000

//Запрос на сервер не выполняется в отдельном потоке. В этом нет необходимости,
// так как интент-сервис и так работает в отдельном потоке.
class DetailsService(name: String = "DetailService") : IntentService(name) {

    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            onEmptyIntent()
        } else {
            val movieId = intent.getIntExtra(MOVIEID_EXTRA, 0)
            if (movieId == 0 ) {
                onEmptyData()
            } else {
                loadMovieService(movieId.toString())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadMovieService(movieId: String) {
        try {
            val uri =
                URL("https://api.themoviedb.org/3/movie/${movieId}?api_key=${BuildConfig.THEMOVIEDB_API_KEY}")
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.apply {
                    requestMethod = REQUEST_GET
                    readTimeout = REQUEST_TIMEOUT
                }

                val movieDtoEx: MovieDtoEx =
                    Gson().fromJson(
                        getLines(BufferedReader(InputStreamReader(urlConnection.inputStream))),
                            MovieDtoEx::class.java
                    )
                onResponse(movieDtoEx)
            } catch (e: Exception) {
                onErrorRequest(e.message ?: "Empty error")
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            onMalformedURL()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    private fun onResponse(movieDtoEx: MovieDtoEx) {
        val result = movieDtoEx.id
        if (result == null) {
            onEmptyResponse()
        } else {
            onSuccessResponse(movieDtoEx.id,movieDtoEx.title, movieDtoEx.overview,movieDtoEx.backdrop, movieDtoEx.ratings,movieDtoEx.runtime, movieDtoEx.reviews)
        }
    }

    // title:String, story: String, backdrop: String, rating: Float, reviews: Int
    private fun onSuccessResponse(id: Int?, title:String?, story: String?, backdrop: String?, rating: Float?,runtime: Int?, reviews: Int?) {
        putLoadResult(DETAILS_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(DETAILS_ID_EXTRA, id)
        broadcastIntent.putExtra(DETAILS_TITLE_EXTRA, title)
        broadcastIntent.putExtra(DETAILS_STORY_EXTRA, story)
        broadcastIntent.putExtra(DETAILS_BACKDROP_EXTRA, backdrop)
        broadcastIntent.putExtra(DETAILS_RATINGS_EXTRA, rating)
        broadcastIntent.putExtra(DETAILS_RUNTIME_EXTRA, runtime)
        broadcastIntent.putExtra(DETAILS_REVIEWS_EXTRA, reviews)

        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }
    //Отправка уведомления
    private fun onMalformedURL() {
        putLoadResult(DETAILS_URL_MALFORMED_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }
    //Отправка уведомления
    private fun onErrorRequest(error: String) {
        putLoadResult(DETAILS_REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(DETAILS_REQUEST_ERROR_MESSAGE_EXTRA, error)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }
    //Отправка уведомления
    private fun onEmptyResponse() {
        putLoadResult(DETAILS_RESPONSE_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }
    //Отправка уведомления
    private fun onEmptyIntent() {
        putLoadResult(DETAILS_INTENT_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }
    //Отправка уведомления
    private fun onEmptyData() {
        putLoadResult(DETAILS_DATA_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }
    //Отправка уведомления
    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, result)
    }
}
