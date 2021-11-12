package com.example.themoviedbgeekkotlin.map.geofence

import com.google.android.gms.maps.model.LatLng

data class LandmarkDataObject(
        val id: String,
        val hint: String,
        val name: String,
        val latLong: LatLng
)
