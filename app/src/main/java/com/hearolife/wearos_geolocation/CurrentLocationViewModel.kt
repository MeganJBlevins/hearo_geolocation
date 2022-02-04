package com.hearolife.wearos_geolocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrentLocationViewModel : ViewModel() {
    private val currentLocation: MutableLiveData<Location> by lazy {
        MutableLiveData<Location>().also {
            loadCurrentLocation()
        }
    }

    fun getCurrentLocation(): LiveData<Location> {
        return currentLocation
    }

    private fun loadCurrentLocation() {
        // Do an asynchronous operation to fetch users.
    }
}