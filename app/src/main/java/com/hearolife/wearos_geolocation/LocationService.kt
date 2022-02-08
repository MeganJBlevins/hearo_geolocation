package com.hearolife.wearos_geolocation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

private var TAG : String = "LocationUtils"
lateinit var fusedLocationProviderClient: FusedLocationProviderClient
lateinit var locationRequest: LocationRequest

data class LocationModel(
    val longitude: Double,
    val latitude: Double,
    val city : String?
)

class LocationService {

    private lateinit var context : Context

    @SuppressLint("MissingPermission")
   fun getLastLocation(context: Context, callbackFunction: String): Task<Location> {
        this.context = context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        Log.d("Debug:", "getting last location")
        var locationInfo = fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
            var location: Location? = task.result
            if (location == null) {
                Log.e(TAG, "Getting new location data")
                NewLocationData(callbackFunction)
            } else {
                if(callbackFunction == "sendToAPI") {
                    sendLocation(location)
                }
                Log.d("Debug:", "Your Location:" + location.longitude)
            }
        }
        return locationInfo
    }

    @SuppressLint("MissingPermission")
    fun NewLocationData(callbackFunction: String): Task<Void> {
        var locationRequest = LocationRequest.create().apply {
            interval = 0
            fastestInterval = 0
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 100
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        if(callbackFunction == "sendToAPI") {
            var request = fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest, sendToAPI, Looper.myLooper()
            )
            return request
        } else {
            var request = fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest, locationCallback, Looper.myLooper()
            )
            return request
        }


    }

    private fun sendLocation(location: Location) {
        var apiService = APIService()
        apiService.sendPost(
            context,
            location.latitude.toString(),
            location.longitude.toString()
        )
    }

    private val sendToAPI = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            var location = locationResult.lastLocation
            sendLocation(location)
        }
    }
        private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            //TODO something here.
        }
    }
}

