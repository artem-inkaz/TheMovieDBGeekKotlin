package com.example.themoviedbgeekkotlin.model

interface MovieListRepository {
    fun getMovieFromServer(): Movie
    fun getMovieFromLocalStorage(): Movie
}