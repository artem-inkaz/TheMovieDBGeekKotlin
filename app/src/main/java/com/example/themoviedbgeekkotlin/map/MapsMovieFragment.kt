package com.example.themoviedbgeekkotlin.map

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
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
import com.example.themoviedbgeekkotlin.*
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.databinding.FragmentMapsMovieBinding
import com.example.themoviedbgeekkotlin.map.api.PlaceInterface
import com.example.themoviedbgeekkotlin.map.geofence.GeofenceBroadcastReceiver
import com.example.themoviedbgeekkotlin.map.geofence.LandmarkDataObject
import com.example.themoviedbgeekkotlin.map.geofence.createChannel
import com.example.themoviedbgeekkotlin.map.repository.MapPlaceRepository
import com.example.themoviedbgeekkotlin.map.responce.NearbyPlacesResponse
import com.example.themoviedbgeekkotlin.map.responce.Place
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
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

    //Geofence
    private lateinit var geofencingClient: GeofencingClient


    private val runningQOrLater =
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R

    private val viewModel: MapMovieViewModel by viewModels { MapMovieViewModelFactory() }

    // TODO #1 GeoFence
    //  A PendingIntent for the Broadcast Receiver that handles geofence transitions.
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(requireContext(), GeofenceBroadcastReceiver::class.java)
        intent.action = ACTION_GEOFENCE_EVENT
        // Use FLAG_UPDATE_CURRENT so that you get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // TODO #2 GeoFence
        geofencingClient = LocationServices.getGeofencingClient(requireContext())

        // запрос разрешения
        checkPermission(
                locationPermissionRequest,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) {
            // проверяем разрешение на доступ к локации, ели есть разрешение то отображаем маркеры
            setUpMaps()
            // получаем адрес текущей локации
            viewModel.getLocation()
            setObservers()
        }

        // Create channel for notifications
        createChannel(requireContext())
    }

    override fun onStart() {
        super.onStart()
        checkPermissionsAndStartGeofencing()
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

    // работает только ф-я определения локации, формирования списка мест не работает с Map AsyncTask
    private fun setObservers() {
        // выводим текущий адрес местоположения устройства
        viewModel.location.observe(viewLifecycleOwner, {
            val location = it ?: return@observe
            Snackbar.make(
                    binding.root,
                    location,
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

    }

    private fun setUpMaps() {
        mapFragment.getMapAsync { googleMap ->
            if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } !=
                    PackageManager.PERMISSION_GRANTED && context?.let {
                        ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION)
                    } != PackageManager.PERMISSION_GRANTED) {
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
                Toast.makeText(context, "${it.latitude},${it.longitude}", Toast.LENGTH_LONG).show()
                url = getUrl(it.latitude, it.longitude, "movie_theater")
                // прорисовка маркеров
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

    // запрос на  сервер получение списка
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

    // прорисовка на карте маркеров
    private fun setPlaceMarker(placeId: List<Place>) {
        var i = 0
        for (place in placeId) {
            i++
            val markerOptions = MarkerOptions()
            val googlePlace: Place = place
            val lat = googlePlace.geometry.location.lat
            val lng = googlePlace.geometry.location.lng
            val placeName = googlePlace.name

            val latLng = LatLng(lat, lng)
            LANDMARK_DATA.add(LandmarkDataObject(placeName, placeName, placeName, latLng))
            addGeofenceForClue(placeName, latLng)
//            addGeofenceForClue()

            markerOptions
                    .position(latLng)
                    .title(placeName)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
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
                    ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION)
                } !=
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
        // Show as marker
        val matchingMarker = markers.firstOrNull {
            val placeTag = (it.tag as? Place) ?: return@firstOrNull false
            return@firstOrNull placeTag == place
        }
        matchingMarker?.showInfoWindow()
    }

    // формирование строки запроса т.к. в строка запроса меняется на стороне Google проще ее сделать вручную
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&rankby=distance&type=restaurant&key=YOUR_API_KEY
    private fun getUrl(latitude: Double, longitude: Double, placeType: String): String {
        val googlePlaceUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
        googlePlaceUrl.append("location=$latitude,$longitude")
        googlePlaceUrl.append("&rankby=distance")
        googlePlaceUrl.append("&type=$placeType")
        googlePlaceUrl.append("&key=" + resources.getString(R.string.browser_key))
        Log.d("getUrl", googlePlaceUrl.toString())
        return googlePlaceUrl.toString()
    }

    /*
    *код для проверки того, что у пользователя включено определение местоположения устройства,
    * и если нет, отобразите действие, в котором они могут его включить.
    */
    // TODO #0 GeoFence Permissions
    private fun checkDeviceLocationSettingsAndStartGeofence(resolve: Boolean = true) {

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val settingsClient = LocationServices.getSettingsClient(requireContext())
        val locationSettingsResponseTask =
                settingsClient.checkLocationSettings(builder.build())

        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(requireContext() as Activity,
                            REQUEST_TURN_DEVICE_LOCATION_ON)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(TAG, "Error geting location settings resolution: " + sendEx.message)
                }
            } else {
                Snackbar.make(
                        binding.map,
                        R.string.location_required_error, Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    checkDeviceLocationSettingsAndStartGeofence()
                }.show()
            }
        }
        locationSettingsResponseTask.addOnCompleteListener {
            if (it.isSuccessful) {
//              addGeofenceForClue()
            }
        }
    }

    /**
     * Starts the permission check and Geofence process only if the Geofence associated with the
     * current hint isn't yet active.
     */
    private fun checkPermissionsAndStartGeofencing() {
        if (foregroundAndBackgroundLocationPermissionApproved()) {
            checkDeviceLocationSettingsAndStartGeofence()
        } else {
            requestForegroundAndBackgroundLocationPermissions()
        }
    }

    private fun foregroundAndBackgroundLocationPermissionApproved(): Boolean {
        val foregroundLocationApproved = (
                PackageManager.PERMISSION_GRANTED ==
                        context?.let {
                            ActivityCompat.checkSelfPermission(it,
                                    Manifest.permission.ACCESS_FINE_LOCATION)
                        })
        val backgroundPermissionApproved =
                if (runningQOrLater) {
                    PackageManager.PERMISSION_GRANTED ==
                            context?.let {
                                ActivityCompat.checkSelfPermission(
                                        it, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                )
                            }
                } else {
                    true
                }
        return foregroundLocationApproved && backgroundPermissionApproved
    }

    /*
 *  Requests ACCESS_FINE_LOCATION and (on Android 10+ (Q) ACCESS_BACKGROUND_LOCATION.
 */
    private fun requestForegroundAndBackgroundLocationPermissions() {
        if (foregroundAndBackgroundLocationPermissionApproved())
            return

        // Else request the permission
        // this provides the result[LOCATION_PERMISSION_INDEX]
        var permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        val resultCode = when {
            runningQOrLater -> {
                // this provides the result[BACKGROUND_LOCATION_PERMISSION_INDEX]
                permissionsArray += Manifest.permission.ACCESS_BACKGROUND_LOCATION
                REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
            }
            else -> REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        }

        Log.d(TAG, "Request foreground only location permission")
        ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsArray,
                resultCode
        )
    }

    /*
 * Adds a Geofence for the current clue if needed, and removes any existing Geofence. This
 * method should be called after the user has granted the location permission.  If there are
 * no more geofences, we remove the geofence and let the viewmodel know that the ending hint
 * is now "active."
 */
    // TODO #3 GeoFence Create

    private fun addGeofenceForClue(name: String, latLong: LatLng) {

//        val currentGeofenceData = LANDMARK_DATA[0]

        // TODO #3.1 Build the Geofence Object Создание геозоны
        val geofence = Geofence.Builder()
                // Задайте идентификатор запроса, строку для идентификации геозоны.
                // TODO
                .setRequestId(name)
                // Задайте круговую область этой геозоны.
                .setCircularRegion(latLong.latitude,
                        latLong.longitude,
                        GEOFENCE_RADIUS_IN_METERS
                )

                .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                // Устанавливаем интересующие типы переходов.
                // Предупреждения генерируются только для этого перехода.
                // В этом примере мы отслеживаем переходы входа и выхода.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build()


        //Создайте запрос геозоны. Установите начальный триггер на INITIAL_TRIGGER_ENTER,
        // добавьте геозону, которую вы только что построили, а затем постройте.
        // Build the geofence request
        val geofencingRequest = GeofencingRequest.Builder()
                // Флаг INITIAL_TRIGGER_ENTER указывает,
                // что сервис геозоны должен запускать уведомление GEOFENCE_TRANSITION_ENTER,
                // когда геозона добавляется и если устройство уже находится внутри этой геозоны.
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                // Добавьте геозоны, которые будут отслеживаться сервисом геозон.
                .addGeofence(geofence)
                .build()

        // Add the new geofence request with the new geofence
        if (requireContext().let { it1 -> ActivityCompat.checkSelfPermission(it1, Manifest.permission.ACCESS_FINE_LOCATION) }
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
            addOnSuccessListener {
                // Geofences added.
                Toast.makeText(requireContext(), R.string.geofences_added,
                        Toast.LENGTH_SHORT)
                        .show()
                Log.e("Add Geofence", geofence.requestId)
            }
            addOnFailureListener {
                // Failed to add geofences.
                Toast.makeText(requireContext(), R.string.geofences_not_added,
                        Toast.LENGTH_SHORT).show()
                if ((it.message != null)) {
                    Log.w(TAG, it.message!!)
                }
            }
        }
    }

    /**
     * Removes geofences. This method should be called after the user has granted the location
     * permission.
     */
    private fun removeGeofences() {
        if (!foregroundAndBackgroundLocationPermissionApproved()) {
            return
        }
        geofencingClient.removeGeofences(geofencePendingIntent).run {
            addOnSuccessListener {
                // Geofences removed
                Log.d(TAG, getString(R.string.geofences_removed))
                Toast.makeText(requireContext(), R.string.geofences_removed, Toast.LENGTH_SHORT)
                        .show()
            }
            addOnFailureListener {
                // Failed to remove geofences
                Log.d(TAG, getString(R.string.geofences_not_removed))
            }
        }
    }

    companion object {
        internal const val ACTION_GEOFENCE_EVENT =
                "MapMovieFragment.themoviedbgeekkotlin.action.ACTION_GEOFENCE_EVENT"
    }

    override fun onDestroy() {
        super.onDestroy()
        removeGeofences()
    }
}

val Location.latLng: LatLng
    get() = LatLng(this.latitude, this.longitude)

