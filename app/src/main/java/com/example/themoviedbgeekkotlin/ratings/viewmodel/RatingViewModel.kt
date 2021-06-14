package com.example.themoviedbgeekkotlin.ratings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themoviedbgeekkotlin.BuildConfig
import com.example.themoviedbgeekkotlin.api.MoviesApi
import com.example.themoviedbgeekkotlin.api.convertMovieDtoToDomain
import com.example.themoviedbgeekkotlin.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RatingViewModel(
    private val apiService: MoviesApi
) : ViewModel() {

    private var debouncePeriod: Long = 500
    private var searchJob: Job? = null


    // 1. Создаём MutableLiveData для передачи данных в View
    private val _searchMoviesLiveData = MutableLiveData<List<Movie>>()
    val searchMoviesLiveData: LiveData<List<Movie>> get() = _searchMoviesLiveData

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

    private fun fetchPopularMovies(lang:String, isAdult: Boolean) {
        viewModelScope.launch {
            // get genres
            val genres = apiService.getGenres()
            // get movie
            val moviesDto = apiService.getMoviesPopular(BuildConfig.THEMOVIEDB_API_KEY,1,lang, isAdult)
            // get movie domain data
            val movies = convertMovieDtoToDomain(moviesDto.results, genres.genres)

            _searchMoviesLiveData.value = movies
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