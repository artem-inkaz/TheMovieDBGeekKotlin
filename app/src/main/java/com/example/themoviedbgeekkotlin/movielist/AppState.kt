package com.example.themoviedbgeekkotlin.movielist

import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.model.MovieGroup
import com.example.themoviedbgeekkotlin.storage.enteties.MovieEntity

sealed class AppState {
    object Init : AppState()
    object Loading : AppState()

    //    data class Error(val error: Throwable) : AppState()
    data class Error(val error: String) : AppState()
    object EmptyDataSet : AppState()
    data class Success(val movie: List<MovieGroup>) : AppState()
    data class SuccessMovie(val movie: List<Movie>) : AppState()
    data class SuccessMovieNotes(val movie: List<MovieEntity>) : AppState()
//    data class Success(val actor: List<Actor>) : AppState()
}