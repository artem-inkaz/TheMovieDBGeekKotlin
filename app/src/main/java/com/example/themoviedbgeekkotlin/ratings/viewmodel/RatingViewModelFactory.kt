package com.example.themoviedbgeekkotlin.ratings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.themoviedbgeekkotlin.App
import com.example.themoviedbgeekkotlin.api.MoviesApi
import com.example.themoviedbgeekkotlin.api.RetrofitModule
import com.example.themoviedbgeekkotlin.movielist.FragmentMovieListViewModel
import com.example.themoviedbgeekkotlin.ratings.repository.MovieSearchRepository
import retrofit2.create

class RatingViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        RatingViewModel::class.java -> RatingViewModel(apiService = RetrofitModule.retrofit.create())
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}