package com.example.themoviedbgeekkotlin.moviesdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themoviedbgeekkotlin.api.MoviesApi
import com.example.themoviedbgeekkotlin.api.convertActorDtoToDomain
import com.example.themoviedbgeekkotlin.model.Actor
import com.example.themoviedbgeekkotlin.model.MovieListRepository
import com.example.themoviedbgeekkotlin.model.MovieListRepositoryImpl
import kotlinx.coroutines.launch
import java.lang.Exception

class FragmentMoviesDetailsViewModel(
    private val apiService: MoviesApi
) : ViewModel() {

    private val _actors = MutableLiveData<List<Actor>>()
    val actors: LiveData<List<Actor>> get() = _actors

    fun getActors(movieId: Int) {
        viewModelScope.launch {
            try {
                // get actors
                val resultRequest = apiService.getActors(movieId = movieId)
                // get actors domain data
                val actors = convertActorDtoToDomain(resultRequest.actors)

                _actors.value = actors

            } catch (e: Exception) {
                Log.e(
                    FragmentMoviesDetailsViewModel::class.java.simpleName,
                    "Error grab actors data ${e.message}"
                )
            }
        }
    }
}