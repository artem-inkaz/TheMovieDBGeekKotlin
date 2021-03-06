package com.example.themoviedbgeekkotlin.moviesdetail

import com.example.themoviedbgeekkotlin.model.Actor

sealed class AppStateActors {
    object Init : AppStateActors()
    object Loading : AppStateActors()

    //    data class Error(val error: Throwable) : AppState()
    data class Error(val error: String) : AppStateActors()
    object EmptyDataSet : AppStateActors()
    data class Success(val actor: List<Actor>) : AppStateActors()
}