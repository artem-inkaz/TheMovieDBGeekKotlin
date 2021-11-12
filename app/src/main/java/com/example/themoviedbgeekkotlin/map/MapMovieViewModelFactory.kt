package com.example.themoviedbgeekkotlin.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.themoviedbgeekkotlin.App
import com.example.themoviedbgeekkotlin.api.MoviesApi
import com.example.themoviedbgeekkotlin.api.RetrofitModule
import com.example.themoviedbgeekkotlin.map.api.PlaceInterface
import com.example.themoviedbgeekkotlin.map.api.RetrofitModulePlace
import com.example.themoviedbgeekkotlin.movielist.FragmentMovieListViewModel

class MapMovieViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MapMovieViewModel::class.java -> App.repositoryPlace()?.let {
            MapMovieViewModel(
                apiPlace = RetrofitModulePlace.retrofit.create(
                        PlaceInterface::class.java
                ),
                locationRepository = it
        )
        }
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}