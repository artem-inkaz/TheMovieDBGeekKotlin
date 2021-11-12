package com.example.themoviedbgeekkotlin.moviesdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.themoviedbgeekkotlin.App
import com.example.themoviedbgeekkotlin.api.MoviesApi
import com.example.themoviedbgeekkotlin.api.RetrofitModule

class MoviesDetailViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        FragmentMoviesDetailsViewModel::class.java -> FragmentMoviesDetailsViewModel(
            apiService = RetrofitModule.retrofit.create(
                MoviesApi::class.java
            ),
            repository = App.repository()
        )
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}