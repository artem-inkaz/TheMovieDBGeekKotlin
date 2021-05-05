package com.example.themoviedbgeekkotlin.model

data class Movie(
    val id: Int = 1,
    val title: String = "Мортал Комбат",
    val overview: String? = "",
    val poster: String? = "",
    val backdrop: String? = "",
    val ratings: Float = 5.0f,
    val adult: Boolean = true,
    val runtime: Int? = 123,
    val reviews: Int = 500,
    val genres: Genre = getDefaultGenre(),
    val like: Boolean = false
)
fun getDefaultGenre() = Genre(0, "Боевик")
