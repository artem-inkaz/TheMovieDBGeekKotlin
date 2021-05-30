package com.example.androidacademy.data

import com.example.themoviedbgeekkotlin.movielist.sectionrecyclerview.TypeMovies

class Database_type_movie {
    fun getTypeMovie(): List<TypeMovies> {
        return listOf(
                TypeMovies("Now Playing"),
                TypeMovies("Up Comming"),
                TypeMovies("Popular"),
                TypeMovies("Top Rated"),
                TypeMovies("Latest")
        )
    }
}