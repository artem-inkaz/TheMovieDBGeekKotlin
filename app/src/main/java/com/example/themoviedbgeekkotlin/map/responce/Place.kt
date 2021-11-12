package com.example.themoviedbgeekkotlin.map.responce

import com.google.android.gms.maps.model.LatLng

data class Place (
    val id: String,
    val geometry: Geometry,
    val icon: String,
    val name: String
)

data class Geometry(
    val location: GeometryLocation
)

data class GeometryLocation(
    val lat: Double,
    val lng: Double
) {
    val latLng: LatLng
        get() = LatLng(lat, lng)
}