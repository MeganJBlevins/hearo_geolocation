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
    val latitude: Double
)

class Location {
    val currentLocation = MutableLiveData<Location>()

    var cityName : String = "Philly"
    var lastLocation : Location? = null

    @SuppressLint("MissingPermission")
    open suspend fun getLastLocation(context: Context): Task<Location> {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        Log.d("Debug:", "getting last location")
        var locationInfo = fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
            var location: Location? = task.result
            if (location == null) {
                Log.e(TAG, "Getting new location data")
                var newLocation = NewLocationData(context)
            } else {
                Log.d("Debug:", "Your Location:" + location.longitude)
                lastLocation = location
                cityName = "Springfield"
            }
        }
        return locationInfo
    }

    @SuppressLint("MissingPermission")
    fun NewLocationData(context: Context): Task<Void> {
        var locationRequest = LocationRequest.create().apply {
            interval = 0
            fastestInterval = 0
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 100
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        var request = fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
        return request
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            currentLocation.postValue(locationResult.lastLocation)
            var prevLocation: Location = locationResult.lastLocation
            Log.d("Debug:","your last last location: "+ prevLocation.longitude.toString())
            lastLocation = prevLocation
            cityName = "Ozark"
        }
    }
}

