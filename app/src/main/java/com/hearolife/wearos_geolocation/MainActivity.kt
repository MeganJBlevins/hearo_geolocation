package com.hearolife.wearos_geolocation

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var permissions: Permissions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
//        var location = Location()
//        var currentLocationText = findViewById(R.id.current_location) as TextView
//        currentLocationText.text = location.cityName

        Log.e(TAG, "getting permissions")
        permissions = Permissions(this)

        Log.e(TAG, "Getting current location")
        if(permissions.allGranted) {
            Log.d(TAG, "Permissions Granted!")
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