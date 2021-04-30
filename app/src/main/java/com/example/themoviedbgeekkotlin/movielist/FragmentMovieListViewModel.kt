package com.example.themoviedbgeekkotlin.movielist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.model.MovieListRepository
import com.example.themoviedbgeekkotlin.model.MovieListRepositoryImpl
import java.util.Random

class FragmentMovieListViewModel(
        private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
        private val repositoryImpl: MovieListRepository = MovieListRepositoryImpl()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    //    fun getMovieFromServer() = getDataFromServer()
    fun getMovieFromLocalStorage() = getDataFromLocalStorage()

    val random = Random()
    private fun rand(from: Int, to: Int): Int {
        return random.nextInt(to - from) + from
    }

    private fun getDataFromLocalStorage() {
        liveDataToObserve.value = AppState.Loading
        rand(10, 200)
        Thread {
            Thread.sleep(200)
            if (random.nextLong() == 180L) liveDataToObserve.postValue(AppState.Success(repositoryImpl.getMovieFromLocalStorage()))
            else liveDataToObserve.postValue(AppState.Error("Ошибка"))
        }.start()
    }
}