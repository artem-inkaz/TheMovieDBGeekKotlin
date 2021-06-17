package com.example.themoviedbgeekkotlin.map.responce

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun convertPlace(nearbyPlacesResponse: List<Place>, location: Location
): List<PlaceId> =
    withContext(Dispatchers.Default) {

        val geometryMap = "${location.latitude},${location.longitude}"

        nearbyPlacesResponse.map{ place: Place ->
            PlaceId(
                id = place.id,
                geometry = geometryMap,
                icon = place.icon,
                name = place.name

            )
        }
    }