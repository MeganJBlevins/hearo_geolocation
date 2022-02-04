package com.hearolife.wearos_geolocation

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrentLocationViewModel(application: Application) : AndroidViewModel(application){

    private val context = getApplication<Application>().applicationContext

    private val locationData = LocationLiveData(context)

    fun getLocationData() = locationData
}