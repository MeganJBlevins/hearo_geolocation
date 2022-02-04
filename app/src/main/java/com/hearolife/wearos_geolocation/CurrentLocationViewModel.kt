package com.hearolife.wearos_geolocation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import java.util.*

class CurrentLocationViewModel : ViewModel() {

    private val currentLocation: MutableLiveData<Location> by lazy {
        MutableLiveData<Location>().also {
            loadCurrentLocation()
        }
    }

    fun getCurrentCity() : String {
        return "Philly"
    }
    fun getCurrentLocation(): LiveData<Location> {
        return currentLocation
    }

    private fun loadCurrentLocation() {
    }
}