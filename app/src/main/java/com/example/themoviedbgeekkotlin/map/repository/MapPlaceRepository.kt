package com.example.themoviedbgeekkotlin.map.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.themoviedbgeekkotlin.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

interface MapPlaceRepository {
    suspend fun currentLocation(): Location?
    suspend fun address(location: Location): String
}

class MapPlaceRepositoryImpl(private val context: Context) : MapPlaceRepository {

    companion object {
        private const val REFRESH_RATE = 10_000L
        private const val THRESHHOLD = 10f

    }

    private val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager



    override suspend fun currentLocation(): Location?
//            withContext(Dispatchers.IO)
            {
                val bestProvider = locationManager.getBestProvider(Criteria(), true)
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                }
                return locationManager.getLastKnownLocation(bestProvider ?: LocationManager.GPS_PROVIDER)
    }

    override suspend fun address(location: Location): String = suspendCoroutine {
                val geocoder = Geocoder(context)

                thread {
                    val result =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1).firstOrNull()

                    if (result == null) {
                        it.resumeWith(Result.success(""))
                    } else {
                        it.resumeWith(Result.success(result.getAddressLine(0)))
                    }
                }
    }

}