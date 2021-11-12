package com.example.themoviedbgeekkotlin.interfaces

import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.storage.enteties.MovieEntity

interface OnItemViewClickListener {

    fun onItemViewClick(movie: Movie)
    fun onItemViewClickNotes(movie: MovieEntity)
}