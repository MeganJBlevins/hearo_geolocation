package com.hearolife.wearos_geolocation

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.hearolife.wearos_geolocation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var geofencingClient: GeofencingClient

    private lateinit var permissions: Permissions
    private lateinit var locationViewModel: CurrentLocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationViewModel = ViewModelProviders.of(this).get(CurrentLocationViewModel::class.java)

        val binding : ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = locationViewModel
        Log.e(TAG, "getting permissions")
        permissions = Permissions(this)
        if(permissions.allGranted) {
            // observer for location data
            locationViewModel.getLocationData().observe(this, Observer {
                binding.currentLongitude.text =  it.longitude.toString()
                binding.currentLatitude.text =  it.latitude.toString()
            })
            Log.d(TAG, "Permissions Granted!")
            Toast.makeText(this@MainActivity, "Permissions Granted",
                Toast.LENGTH_SHORT).show()
            geofencingClient = LocationServices.getGeofencingClient(this)
        }
    }

    fun setGeofence(view: View) {
        if(!permissions.allGranted) {
            Toast.makeText(this@MainActivity, "Permissions Not Set",
                Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@MainActivity, "Yaaaayyyy!",
                Toast.LENGTH_SHORT).show()
        }
        Log.e(TAG, "Setting Geofence")
    }
}

private val TAG: String = "Main Activity"