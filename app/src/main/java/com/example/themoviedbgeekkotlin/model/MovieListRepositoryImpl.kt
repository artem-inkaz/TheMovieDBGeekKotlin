package com.example.themoviedbgeekkotlin.model

import com.example.androidacademy.data.Database_actors
import com.example.androidacademy.data.Database_movies

class MovieListRepositoryImpl: MovieListRepository {

    override fun getMovieFromServer() = Movie()

    override fun getMovieFromLocalStorage() = Database_movies().getMovies()

    override fun getActorFromServer() = Actor()

    override fun getActorFromLocalStorage() = Database_actors().getActors()
}