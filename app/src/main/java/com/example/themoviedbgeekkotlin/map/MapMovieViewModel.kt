package com.example.themoviedbgeekkotlin.map

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themoviedbgeekkotlin.map.api.PlaceInterface
import com.example.themoviedbgeekkotlin.map.repository.MapPlaceRepository
import com.example.themoviedbgeekkotlin.map.responce.NearbyPlacesResponse
import com.example.themoviedbgeekkotlin.map.responce.PlaceId
import com.example.themoviedbgeekkotlin.map.responce.convertPlace
import com.example.themoviedbgeekkotlin.movielist.AppState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.lang.Exception

class MapMovieViewModel(
        private val apiPlace: PlaceInterface,
        private val locationRepository: MapPlaceRepository
        ): ViewModel() {


    private val _state = MutableLiveData<AppState>(AppState.Init)
    val state: LiveData<AppState> get() = _state

    private val _mutableLiveDataLocation = MutableLiveData<String>()
    val location: LiveData<String> get() = _mutableLiveDataLocation

    private val _mutableLiveDataPlaces = MutableLiveData<List<PlaceId>>(emptyList())
    val listPlaces: LiveData<List<PlaceId>> get() = _mutableLiveDataPlaces

    private val _mutableLiveDataPlacesId = MutableLiveData<List<NearbyPlacesResponse>>(emptyList())
    val listPlacesId: LiveData<List<NearbyPlacesResponse>> get() = _mutableLiveDataPlacesId

    fun getLocation(){
        viewModelScope.launch {
            try {
                _state.value = AppState.Loading

                locationRepository.currentLocation()?.let {

                    val address = locationRepository.address(it)
                    _mutableLiveDataLocation.value = address

                    _state.value = AppState.SuccessLocation(address)
                }

            } catch (e: Exception) {
                _state.value = AppState.Error("Ошибка получения данных")
                Log.e(ViewModel::class.java.simpleName, "Error grab places data ${e.message}")
            }
        }
    }


    fun updateData(location: Location, radius: Int, typePlace: String, key: String) {

        viewModelScope.launch {
            try {
                _state.value = AppState.Loading

              val places = apiPlace.nearbyPlaces("${location.latitude},${location.longitude}",radius, typePlace,key)
//                val places = apiPlace.nearbyPlaces(location.toString(),radius, typePlace, key)

                val placeId = convertPlace(places.results, location)

                _mutableLiveDataPlaces.value = placeId
                _state.value = AppState.SuccessPlace(places.results)

            } catch (e: Exception) {
                _state.value = AppState.Error("Ошибка получения данных")
                Log.e(ViewModel::class.java.simpleName, "Error grab places data ${e.message}")
            }
        }
    }

    fun fetchData(url: String) {

        viewModelScope.launch {
            try {
                _state.value = AppState.Loading

                val places = apiPlace.getNearByPlaces(url)

                _mutableLiveDataPlacesId.value = listOf(places)

                _state.value = AppState.SuccessPlace(places.results)

            } catch (e: Exception) {
                _state.value = AppState.Error("Ошибка получения данных")
                Log.e(ViewModel::class.java.simpleName, "Error grab places data ${e.message}")
            }
        }
    }


}