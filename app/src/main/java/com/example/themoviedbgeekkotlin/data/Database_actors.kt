package com.example.androidacademy.data

import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.model.Actor

class Database_actors {
    fun getActors() = listOf(
            Actor("Robert", "Downey Jr.", R.drawable.movie_downey),
            Actor("Chris", "Evans", R.drawable.movie_evans),
            Actor("Mark", "Ruffalo", R.drawable.movie_ruffalo),
            Actor("Chris", "Hemsworth", R.drawable.movie_hemsworth)
    )
}