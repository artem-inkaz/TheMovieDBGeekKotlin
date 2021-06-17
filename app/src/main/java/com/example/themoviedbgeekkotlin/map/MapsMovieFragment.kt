package com.example.themoviedbgeekkotlin.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.databinding.FragmentMapsMovieBinding
import com.example.themoviedbgeekkotlin.map.api.PlaceInterface
import com.example.themoviedbgeekkotlin.map.repository.MapPlaceRepository
import com.example.themoviedbgeekkotlin.map.responce.NearbyPlacesResponse
import com.example.themoviedbgeekkotlin.map.responce.Place
import com.example.themoviedbgeekkotlin.movielist.AppState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class MapsMovieFragment : Fragment() {

    private val TAG = "MapActivity"

    private var _binding: FragmentMapsMovieBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var placesService: PlaceInterface
    private var map: GoogleMap? = null
    private var places: List<Place>? = null
    private var currentLocation: Location? = null
    private lateinit var mMarker: Marker //= Marker()

    lateinit var url: String
    private var markers: MutableList<Marker> = emptyList<Marker>().toMutableList()
    // Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var repositoryLocation: MapPlaceRepository

    private val viewModel: MapMovieViewModel by viewModels { MapMovieViewModelFactory() }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private val locationPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    viewModel.getLocation()

                } else {
                    Toast.makeText(
                            requireContext(),
                            "Too sad, you cannot use this amazing feature!",
                            Toast.LENGTH_LONG
                    ).show()
                    // Failed pass
                }
            }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentMapsMovieBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment = (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
        mapFragment.getMapAsync(callback)
        placesService = PlaceInterface.create()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context)

        // запрос разрешения
        checkPermission(
                locationPermissionRequest,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) {

            setUpMaps()

            // проверяем разрешение на доступ к локации, ели есть разрешение то отображаем маркеры
            viewModel.getLocation()
            setObservers()
        }
    }

    private fun checkPermission(
            permissionLauncher: ActivityResultLauncher<String>,
            permission: String,
            onGranted: () -> Unit
    ) {
        when {
            (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
            )) == PackageManager.PERMISSION_GRANTED -> {
                onGranted.invoke()
            }

            shouldShowRequestPermissionRationale(permission) -> {
                Snackbar.make(
                        binding.root,
                        "I need this permission very much!",
                        Snackbar.LENGTH_INDEFINITE
                ).setAction(
                        "Grant"
                ) {
                    permissionLauncher.launch(permission)
                }.show()
            }

            else -> {
                permissionLauncher.launch(permission)
            }
        }

    }


    private fun setObservers() {
        // выводим текущий адрес местоположения устройства
        viewModel.location.observe(viewLifecycleOwner, {
            val location = it ?: return@observe
            Snackbar.make(
                    binding.root,
                    "${location}",
                    Snackbar.LENGTH_SHORT
            ).show()
        })


//        viewModel.listPlacesId.observe(viewLifecycleOwner,{
//            val plaseId = it ?: return@observe
//           for (place in this.places!!){
// //           for (i in 0 until (it?.size ?: 0)) {
//                val markerOptions = MarkerOptions()
//                val googlePlace: Place = place
//                val lat = googlePlace.geometry.location.lat.toDouble()
//                val lng = googlePlace.geometry.location.lng.toDouble()
//               val placeName = googlePlace.name
//
//               val latLng = LatLng(lat, lng)
//                markerOptions
//                    .position(latLng)
//                    .title(placeName)
//
//                    markerOptions.icon( BitmapDescriptorFactory.defaultMarker(
//                        BitmapDescriptorFactory.HUE_RED
//                    )
//                )
//                mMarker = map?.addMarker(markerOptions)!!
//                map?.addMarker(markerOptions)
//                map?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
//                map?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
//                map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
//
//            }
//       })

        // observe status
        viewModel.state.observe(viewLifecycleOwner, { status ->
            when (status) {
                is AppState.Init, is AppState.Success -> {
//                    binding.progressBar?.visibility = View.INVISIBLE
                }
                is AppState.Loading -> {
//                    binding.progressBar?.visibility = View.VISIBLE
                }
                is AppState.Error -> {
//                    binding.progressBar?.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun setUpMaps() {
        mapFragment.getMapAsync { googleMap ->
            if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } !=
                    PackageManager.PERMISSION_GRANTED && context?.let {
                        ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@getMapAsync
            }
            googleMap.isMyLocationEnabled = true
            getCurrentLocation {
                val pos = CameraPosition.fromLatLngZoom(it.latLng, 50f)
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos))
                Toast.makeText(context,"${it.latitude},${it.longitude}",Toast.LENGTH_LONG).show()
                url= getUrl(it.latitude, it.longitude, "movie_theater")
                getNearbyPlaces(url)
            }
            googleMap.setOnMarkerClickListener { marker ->
                val tag = marker.tag
                if (tag !is Place) {
                    return@setOnMarkerClickListener false
                }
                showInfoWindow(tag)
                return@setOnMarkerClickListener true
            }
            map = googleMap
        }
    }

    private fun getNearbyPlaces(url: String) {
        placesService.getNearByPlacesCall(url)
            .enqueue(
            object : Callback<NearbyPlacesResponse> {
                override fun onFailure(call: Call<NearbyPlacesResponse>, t: Throwable) {
                    Log.e(TAG, "Failed to get nearby places", t)
                }

                override fun onResponse(
                    call: Call<NearbyPlacesResponse>,
                    response: Response<NearbyPlacesResponse>
                ) {
                    if (!response.isSuccessful) {
                        Log.e(TAG, "Failed to get nearby places")
                        return
                    }

                    val place = response.body()?.results ?: emptyList()
                    places = place
                    setPlaceMarker(places!!)
                }
            }
        )
    }

    private fun setPlaceMarker(placeId: List<Place>) {

        for (place in placeId!!){
            val markerOptions = MarkerOptions()
            val googlePlace: Place = place
            val lat = googlePlace.geometry.location.lat
            val lng = googlePlace.geometry.location.lng
            val placeName = googlePlace.name

            val latLng = LatLng(lat, lng)
            markerOptions
                .position(latLng)
                .title(placeName)
            markerOptions.icon( BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_RED
            )
            )
            mMarker = map?.addMarker(markerOptions)!!
            map?.addMarker(markerOptions)
            map?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            map?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))

        }
    }

    private fun getCurrentLocation(onSuccess: (Location) -> Unit) {
        if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } !=
                PackageManager.PERMISSION_GRANTED && context?.let {
                    ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            currentLocation = location
            onSuccess(location)
        }.addOnFailureListener {
            Log.e(TAG, "Could not get location")
        }
    }

    private fun showInfoWindow(place: Place) {
        // Show in AR
//        val matchingPlaceNode = anchorNode?.children?.filter {
//            it is PlaceNode
//        }?.first {
//            val otherPlace = (it as PlaceNode).place ?: return@first false
//            return@first otherPlace == place
//        } as? PlaceNode
//        matchingPlaceNode?.showInfoWindow()

        // Show as marker
        val matchingMarker = markers.firstOrNull {
            val placeTag = (it.tag as? Place) ?: return@firstOrNull false
            return@firstOrNull placeTag == place
        }
        matchingMarker?.showInfoWindow()
    }
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&rankby=distance&type=restaurant&key=YOUR_API_KEY
    private fun getUrl(latitude: Double, longitude: Double, placeType: String):String {
        val googlePlaceUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
        googlePlaceUrl.append("location=$latitude,$longitude")
        googlePlaceUrl.append("&rankby=distance")
        googlePlaceUrl.append("&type=$placeType")
        googlePlaceUrl.append("&key="+resources.getString(R.string.browser_key))
        Log.d("getUrl",googlePlaceUrl.toString())
        return googlePlaceUrl.toString()
    }
}

val Location.latLng: LatLng
    get() = LatLng(this.latitude, this.longitude)