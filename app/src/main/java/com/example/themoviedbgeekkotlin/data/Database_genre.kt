package com.example.androidacademy.data

import com.example.themoviedbgeekkotlin.model.Genre

class Database_genre {
    fun getGenres(): List<Genre> {
        return listOf(
                Genre(1, "Action, Adventure, Drama"),
                Genre(2, "Action, Sci-Fi, Thriller"),
                Genre(3, "Action, Adventure, Fantasy"),
                Genre(4, "Action, Adventure, Sci-Fi")
        )
    }
}