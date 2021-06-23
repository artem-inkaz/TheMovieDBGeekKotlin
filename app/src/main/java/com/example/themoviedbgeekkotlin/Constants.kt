package com.example.themoviedbgeekkotlin

import com.example.themoviedbgeekkotlin.map.geofence.LandmarkDataObject
import com.example.themoviedbgeekkotlin.map.responce.Place
import java.util.concurrent.TimeUnit

// объявляем глобальные переменные
lateinit var APP_ACTIVITY: MainActivity

const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
const val TAG = "MapMoveFrafment"
const val LOCATION_PERMISSION_INDEX = 0
const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1
const val GEOFENCE_RADIUS_IN_METERS = 100f
const val EXTRA_GEOFENCE_INDEX = "GEOFENCE_INDEX"
const val CHANNEL_ID = "NOW_PLAYING"
const val TOPIC_ID = "PLAYING"
//var LANDMARK_DATA = arrayListOf<LandmarkDataObject>()
var LANDMARK_DATA : MutableList<LandmarkDataObject> = ArrayList()
/**
 * Used to set an expiration time for a geofence. After this amount of time, Location services
 * stops tracking the geofence. For this sample, geofences expire after one hour.
 */
val GEOFENCE_EXPIRATION_IN_MILLISECONDS: Long = TimeUnit.HOURS.toMillis(1)