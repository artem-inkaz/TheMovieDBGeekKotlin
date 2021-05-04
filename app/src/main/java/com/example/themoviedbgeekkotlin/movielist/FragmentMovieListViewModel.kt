package com.example.themoviedbgeekkotlin.movielist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.model.MovieListRepository
import com.example.themoviedbgeekkotlin.model.MovieListRepositoryImpl
import java.util.Random

class FragmentMovieListViewModel(
        private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
) : ViewModel() {
    private val repositoryImpl: MovieListRepository = MovieListRepositoryImpl()
    fun getLiveData() = liveDataToObserve

    //    fun getMovieFromServer() = getDataFromServer()
    fun getMovieFromLocalStorage() = getDataFromLocalStorage()

    fun getData(): LiveData<AppState> {
        getMovieFromLocalStorage()
        return liveDataToObserve
    }

    private fun getDataFromLocalStorage() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            Thread.sleep(200)
            liveDataToObserve.postValue(AppState.Success(repositoryImpl.getMovieFromLocalStorage()))
        }.start()
    }
}