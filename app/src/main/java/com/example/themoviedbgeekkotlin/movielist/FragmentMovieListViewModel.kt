package com.example.themoviedbgeekkotlin.movielist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themoviedbgeekkotlin.api.MoviesApi
import com.example.themoviedbgeekkotlin.api.convertMovieDtoToDomain
import com.example.themoviedbgeekkotlin.api.convertMovieDtoToDomainV2
import com.example.themoviedbgeekkotlin.api.convertToMovieGroup
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.model.MovieGroup
import com.example.themoviedbgeekkotlin.model.MovieListRepository
import com.example.themoviedbgeekkotlin.model.MovieListRepositoryImpl
import kotlinx.coroutines.launch
import java.util.Random

class FragmentMovieListViewModel(
        private val apiServiceMovie: MoviesApi
) : ViewModel() {

    private val _state = MutableLiveData<AppState>(AppState.Init)
    val state: LiveData<AppState> get() = _state

    private val _mutableLiveDataMovies = MutableLiveData<List<MovieGroup>>()
    val listMovies: LiveData<List<MovieGroup>> get() = _mutableLiveDataMovies

    fun updateDate() {

        viewModelScope.launch {
            try {
                _state.value = AppState.Loading
                // получаем genres (жанры)
               val genres = apiServiceMovie.getGenres()
                // получаем фильмы по типам
                val moviesDtoNowPlaying = apiServiceMovie.getNowPlaying()
                val moviesDtoPoular = apiServiceMovie.getMoviesPopular()
                val moviesDtoTopRated = apiServiceMovie.getTopRated()
                val moviesDtoUpComming = apiServiceMovie.getUpComming()
                val nameGroup = "Now Playing"
                val moviesNowPlaying = convertMovieDtoToDomainV2(nameGroup,moviesDtoNowPlaying.results,genres.genres)
                val moviesNowPlayingV2 = convertToMovieGroup(nameGroup,moviesNowPlaying)
//                val moviesPoular = convertMovieDtoToDomain(moviesDtoPoular.results,genres.genres)
//                val moviesTopRated = convertMovieDtoToDomain(moviesDtoTopRated.results,genres.genres)
//                val moviesUpComming = convertMovieDtoToDomain(moviesDtoUpComming.results,genres.genres)

                _mutableLiveDataMovies.value = listOf(moviesNowPlayingV2)
                _state.value = AppState.Success(listOf(moviesNowPlayingV2))

            } catch (e:Exception) {
                _state.value = AppState.Error("Ошибка загрузки данных")
                Log.e(ViewModel::class.java.simpleName, "Error grab movies data ${e.message}")
            }
        }
    }

}