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
import com.hearolife.wearos_geolocation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var permissions: Permissions
    private val viewModel by lazy { ViewModelProviders.of(this).get(CurrentLocationViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        Log.e(TAG, "getting permissions")
        permissions = Permissions(this)
        if(permissions.allGranted) {
            Log.d(TAG, "Permissions Granted!")
            Toast.makeText(this@MainActivity, "Permissions Granted",
                Toast.LENGTH_SHORT).show()
//            Log.e(TAG, "city Name: " + location.cityName)
//            var location = Location()
//            var currentLocationText = findViewById(R.id.current_location) as TextView
//            currentLocationText.text = location.cityName

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