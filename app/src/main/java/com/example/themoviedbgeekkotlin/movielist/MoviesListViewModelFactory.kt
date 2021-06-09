package com.example.themoviedbgeekkotlin.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.themoviedbgeekkotlin.api.MoviesApi
import com.example.themoviedbgeekkotlin.api.RetrofitModule

class MoviesListViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        FragmentMovieListViewModel::class.java -> FragmentMovieListViewModel(
            apiServiceMovie = RetrofitModule.retrofit.create(
                MoviesApi::class.java
            )
        )
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}