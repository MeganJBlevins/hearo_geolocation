package com.hearolife.wearos_geolocation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private val runningQOrLater =
    android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

private const val TAG: String = "Permissions"
private const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
val PERMISSION_ID = 1010

class Permissions(context: Context) {
    var allGranted: Boolean = false
        get() = backgroundLocation && foregroundLocation

    private var backgroundLocation : Boolean = false
    private var foregroundLocation : Boolean = false
    private var internet : Boolean = false

    init {
        var granted = getPermission(context)
        Log.e(TAG, "All granted $granted")
        allGranted = granted
    }

    fun getPermission(context: Context): Boolean {
        backgroundLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PERMISSION_GRANTED
        foregroundLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PERMISSION_GRANTED
        internet = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.INTERNET
        ) == PERMISSION_GRANTED

        if (!backgroundLocation || !foregroundLocation || !internet) {
            requestPermissions(context)
        }
        return foregroundLocation && backgroundLocation
    }

    fun requestPermissions(context: Context) {
        Log.e(TAG, "requesting permissions")
        val activity = context as Activity
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )

        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PERMISSION_GRANTED -> {
                Log.d(TAG, "Permissions Granted")
                // You can use the API that requires the permission.
            }
            else -> {
                Log.d(TAG, "Permissions Not Granted. Need more info.")
            }
        }

//        getPermission(context)
    }
}
