package com.example.themoviedbgeekkotlin.movielist

import com.example.themoviedbgeekkotlin.model.Actor
import com.example.themoviedbgeekkotlin.model.Movie

sealed class AppState {
    object Init : AppState()
    object Loading : AppState()
//    data class Error(val error: Throwable) : AppState()
    data class Error(val error: String) : AppState()
    object EmptyDataSet : AppState()
    data class Success(val movie: List<Movie>) : AppState()
//    data class Success(val actor: List<Actor>) : AppState()
}