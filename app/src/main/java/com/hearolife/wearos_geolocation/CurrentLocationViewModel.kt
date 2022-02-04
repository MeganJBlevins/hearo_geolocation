package com.hearolife.wearos_geolocation

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrentLocationViewModel : ViewModel() {

    private val _city = MutableLiveData("Philadelphia")
    private val _hello = MutableLiveData("Goodbye")

    val city: LiveData<String> = _city
    val hello: LiveData<String> = _hello

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

    fun sayHello() {
        _hello.value = "Hello"
    }


    private fun loadCurrentLocation() {
    }
}