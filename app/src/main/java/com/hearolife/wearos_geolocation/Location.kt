package com.hearolife.wearos_geolocation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*

private var TAG : String = "LocationUtils"
lateinit var fusedLocationProviderClient: FusedLocationProviderClient
lateinit var locationRequest: LocationRequest


class Location : Fragment(R.layout.location) {
    private var mContext: Context? = null

    var cityName : String = "Philly"
    var lastLocation : Location? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        getLastLocation()
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        if(mContext != null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext)

            Log.d("Debug:", "getting last location")
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                var location: Location? = task.result
                if (location == null) {
                    Log.e(TAG, "Getting new location data")
                    NewLocationData()
                } else {
                    Log.d("Debug:", "Your Location:" + location.longitude)
                    lastLocation = location
                    cityName = "Philly"
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun NewLocationData(){
        var locationRequest = LocationRequest.create().apply {
            interval = 0
            fastestInterval = 0
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 100
        }
        if(mContext != null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest, locationCallback, Looper.myLooper()
            )
        }
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var prevLocation: Location = locationResult.lastLocation
            Log.d("Debug:","your last last location: "+ prevLocation.longitude.toString())
            lastLocation = prevLocation
            cityName = "Ozark"
        }
    }
}

