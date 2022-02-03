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
        Log.e(TAG, "getting permissions")
        permissions = Permissions(this)
        if(permissions.allGranted) {
            Log.d(TAG, "Permissions Granted!")
            Toast.makeText(this@MainActivity, "Permissions Granted",
                Toast.LENGTH_SHORT).show()
//            var location = Location()
//            var retrieveLocation =  location.getLastLocation(this)
//            Log.e(TAG, "city Name: " + location.cityName)
//            var currentLocationText = findViewById(R.id.current_location) as TextView
//            currentLocationText.text = location.cityName

        }
    }

    fun sayHello(view: View) {
        Toast.makeText(this@MainActivity, "Hello there!",
            Toast.LENGTH_SHORT).show()
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