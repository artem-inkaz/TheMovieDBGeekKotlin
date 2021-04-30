package com.example.themoviedbgeekkotlin.model

class MovieListRepositoryImpl: MovieListRepository {

    override fun getMovieFromServer(): Movie {
        return Movie()
    }

    override fun getMovieFromLocalStorage(): Movie {
       return Movie()
    }
}