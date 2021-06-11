package com.example.themoviedbgeekkotlin.movielist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themoviedbgeekkotlin.api.MoviesApi
import com.example.themoviedbgeekkotlin.api.convertMovieDtoToDomainV2
import com.example.themoviedbgeekkotlin.api.convertToMovieGroup
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.model.MovieGroup
import com.example.themoviedbgeekkotlin.repository.MoviesRepository
import kotlinx.coroutines.launch

class FragmentMovieListViewModel(
    private val apiServiceMovie: MoviesApi,
    private val repository: MoviesRepository
) : ViewModel() {

    private val _state = MutableLiveData<AppState>(AppState.Init)
    val state: LiveData<AppState> get() = _state

    private val _mutableLiveDataMovies = MutableLiveData<List<MovieGroup>>()
    val listMovies: LiveData<List<MovieGroup>> get() = _mutableLiveDataMovies

    var listMovieGroup = listOf<MovieGroup>()

    fun updateDate() {
        loadMoviesFromApi()
    }

    fun loadMoviesFromApi() {

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
                val nameGroup1 = "Now Playing"
                val nameGroup2 = "Poular"
                val nameGroup3 = "TopRated"
                val nameGroup4 = "Up Comming"

                val moviesNowPlaying = convertMovieDtoToDomainV2(
                    nameGroup1,
                    moviesDtoNowPlaying.results,
                    genres.genres
                )
                val moviesNowPlayingV2 =
                    convertToMovieGroup(nameGroup1, moviesNowPlaying as ArrayList<Movie>)

                val moviesPoular =
                    convertMovieDtoToDomainV2(nameGroup2, moviesDtoPoular.results, genres.genres)
                val moviesPoularV2 =
                    convertToMovieGroup(nameGroup2, moviesPoular as ArrayList<Movie>)

                val moviesTopRated =
                    convertMovieDtoToDomainV2(nameGroup3, moviesDtoTopRated.results, genres.genres)
                val moviesTopRatedV2 =
                    convertToMovieGroup(nameGroup3, moviesTopRated as ArrayList<Movie>)

                val moviesUpComming =
                    convertMovieDtoToDomainV2(nameGroup4, moviesDtoUpComming.results, genres.genres)
                val moviesUpCommingV2 =
                    convertToMovieGroup(nameGroup4, moviesUpComming as ArrayList<Movie>)

                listMovieGroup =listOf(moviesNowPlayingV2, moviesPoularV2, moviesTopRatedV2, moviesUpCommingV2)

                _mutableLiveDataMovies.value = listMovieGroup
                _state.value = AppState.Success(listMovieGroup)

                // don't rewrite with empty data
                if (!listMovieGroup.isNullOrEmpty()) {
                    saveMoviesLocally()
                }

            } catch (e: Exception) {
                if (state.value != AppState.Success(listMovieGroup)) {
                    _state.value = AppState.Error("Ошибка загрузки данных")
                }
                Log.e(ViewModel::class.java.simpleName, "Error grab movies data ${e.message}")
            }
        }
    }

    private fun saveMoviesLocally() {
        if (!listMovies.value.isNullOrEmpty()) {
            viewModelScope.launch {
//                repository.rewriteMoviesListIntoDB(listMovies.value!!)
            }
        }
    }
}