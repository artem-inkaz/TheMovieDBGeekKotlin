package com.example.themoviedbgeekkotlin.ratings.repository

import com.example.themoviedbgeekkotlin.BuildConfig
import com.example.themoviedbgeekkotlin.api.MovieDto
import com.example.themoviedbgeekkotlin.api.MoviesApi
import com.example.themoviedbgeekkotlin.model.Movie

class MovieSearchRepository(private val apiServiceMovie: MoviesApi) {

    suspend fun fetchNowPlayingMovies(): List<MovieDto>? {
        val deferredResponse =
                apiServiceMovie.getMoviesPopular(BuildConfig.THEMOVIEDB_API_KEY, 1, "ru", true)
        return deferredResponse.results
    }

    suspend fun findMoviesByQuery(queryText: String): List<MovieDto>? {
        val deferredResponse = apiServiceMovie.findMovies(BuildConfig.THEMOVIEDB_API_KEY, queryText, "ru", true, "RU")
        return deferredResponse.results
    }
}