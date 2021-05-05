package com.example.themoviedbgeekkotlin.interfaces

import com.example.themoviedbgeekkotlin.model.Movie

interface OnItemViewClickListener {
    fun onItemViewClick(movie: Movie)
}