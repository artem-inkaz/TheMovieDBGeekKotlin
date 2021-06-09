package com.example.themoviedbgeekkotlin.moviesdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themoviedbgeekkotlin.model.MovieListRepository
import com.example.themoviedbgeekkotlin.model.MovieListRepositoryImpl

class FragmentMoviesDetailsViewModel(
        private val liveDataToObserve: MutableLiveData<AppStateActors> = MutableLiveData())
 : ViewModel() {
    private val repositoryImpl: MovieListRepository = MovieListRepositoryImpl()

    fun getLiveData() = liveDataToObserve

    //    fun getActorFromServer() = getDataFromServer()
//    fun getActorFromLocalStorage() = getDataFromLocalStorage()

//    fun getData(): LiveData<AppStateActors> {
//        getActorFromLocalStorage()
//        return liveDataToObserve
//    }

//    private fun getDataFromLocalStorage() {
//        liveDataToObserve.value = AppStateActors.Loading
//        Thread {
//            Thread.sleep(200)
//            liveDataToObserve.postValue(AppStateActors.Success(repositoryImpl.getActorFromLocalStorage()))
//        }.start()
//    }
}