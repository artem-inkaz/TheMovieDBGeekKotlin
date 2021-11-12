package com.example.themoviedbgeekkotlin.ratings.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.themoviedbgeekkotlin.BuildConfig
import com.example.themoviedbgeekkotlin.api.MoviesApi
import com.example.themoviedbgeekkotlin.api.convertMovieDtoToDomain
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.movielist.AppState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RatingViewModel(
    private val apiService: MoviesApi
) : ViewModel() {

    private var debouncePeriod: Long = 500
    private var searchJob: Job? = null

    private val _state = MutableLiveData<AppState>(AppState.Init)
    val state: LiveData<AppState> get() = _state

    // 1. Создаём MutableLiveData для передачи данных в View
    private val _popularMoviesLiveData = MutableLiveData<List<Movie>>()
    private val _searchMoviesLiveData = MutableLiveData<List<Movie>>()
    val searchMoviesLiveData: LiveData<List<Movie>> get() = _searchMoviesLiveData

    val moviesMediatorData = MediatorLiveData<List<Movie>>()

    init {

        // 1
        moviesMediatorData.addSource(_popularMoviesLiveData) {
            moviesMediatorData.value = it
        }

        // 2
        moviesMediatorData.addSource(_searchMoviesLiveData) {
            moviesMediatorData.value = it
        }

    }

    // 2. Вызывается из View для передачи строки поиска в сетевой запрос
    fun onSearchQuery(query: String,lang:String, isAdult: Boolean, region: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(debouncePeriod)
            if (query.length > 2) {
                fetchMovieByQuery(query,lang, isAdult, region)
            }
        }
    }

    fun fetchPopularMovies(lang:String, isAdult: Boolean) {
        viewModelScope.launch {
            try {
                _state.value = AppState.Loading
                // get genres
                val genres = apiService.getGenres()
                // get movie
                val moviesDto = apiService.getMoviesPopular(BuildConfig.THEMOVIEDB_API_KEY,1,lang, isAdult)
                // get movie domain data
                val movies = convertMovieDtoToDomain(moviesDto.results, genres.genres)

                _popularMoviesLiveData.value = movies

                _state.value = AppState.SuccessMovie(movies)
            } catch (e: Exception) {
                _state.value = AppState.Error("Ошибка загрузки данных")
                Log.e(ViewModel::class.java.simpleName, "Error grab movies data ${e.message}")
            }
        }
    }

    // 3. Используя query api
    private fun fetchMovieByQuery(query: String,lang:String, isAdult: Boolean, region: String) {
        viewModelScope.launch {
            // get genres
            val genres = apiService.getGenres()
            val moviesDto = apiService.findMovies(BuildConfig.THEMOVIEDB_API_KEY,query,lang, isAdult, region)
            // get movie domain data
            val movies = convertMovieDtoToDomain(moviesDto.results, genres.genres)
            _searchMoviesLiveData.value = movies
        }
    }

    // Вызывается для очищения ресурсов
    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}