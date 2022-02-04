package com.hearolife.wearos_geolocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrentLocationViewModel : ViewModel() {

    val city : String = "Philadelphia"

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