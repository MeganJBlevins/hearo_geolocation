package com.hearolife.wearos_geolocation

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.*
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.firebase.FirebaseApp
import com.hearolife.wearos_geolocation.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    lateinit var geofencingClient: GeofencingClient
    private var locationManager : LocationManager? = null

    var geofence : Geofence? = null
    private lateinit var firebaseService : MyFirebaseMessagingService
    private lateinit var locationViewModel: CurrentLocationViewModel

    // permissions
    private var PERMISSION_ID : Int = 0
    private var coarseLocation : Boolean = false
    private var fineLocation : Boolean = false
    private var backgroundLocation : Boolean = false
    private var internet : Boolean = false

    var permissionsGranted: Boolean = false
        get() = coarseLocation && fineLocation && internet && backgroundLocation

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationViewModel = ViewModelProviders.of(this).get(CurrentLocationViewModel::class.java)
        if (!hasGps()) {
            Log.d(TAG, "This hardware doesn't have GPS.")
            // Fall back to functionality that doesn't use location or
            // warn the user that location function isn't available.
        }
        firebaseService = MyFirebaseMessagingService()
        firebaseService.getToken(this)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        val binding : ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = locationViewModel

        if(!permissionsGranted) {
            requestPermissions()
        }

        Log.d(TAG, "Permissions Granted!")

        // observer for location data
        locationViewModel.getLocationData().observe(this, Observer {
            binding.currentLongitude.text =  it.longitude.toString()
            binding.currentLatitude.text =  it.latitude.toString()
        })

        // no longer using worker... using alarm.
//        val sendLocationWorkRequest =
//            PeriodicWorkRequestBuilder<SendLocationWorker>(15, TimeUnit.MINUTES)
//                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
//                .build()
//
//        WorkManager
//            .getInstance(this)
//            .enqueue(sendLocationWorkRequest)

        val alarm = LocationAlarm()
        alarm.setAlarm(this);

        // set geofence
        geofencingClient = LocationServices.getGeofencingClient(this)

    }

    // make sure watch has GPS
    private fun hasGps(): Boolean =
        packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)

    @SuppressLint("MissingPermission")
    fun setGeofence(view: View) {
        var latitude: Double? = null
        var longitude: Double? = null
        locationViewModel.getLocationData().observe(this, Observer {
            latitude = it.latitude
            longitude = it.longitude
        })

        if(!permissionsGranted) {
            requestPermissions()
            Toast.makeText(this@MainActivity, "Permissions Not Set",
                Toast.LENGTH_SHORT).show()
        }

        if(latitude != null && longitude != null) {
            geofence = buildGeofence(latitude!!, longitude!!)
            geofencingClient?.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
                addOnSuccessListener {
                    Toast.makeText(
                        this@MainActivity, "Geofence set!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                addOnFailureListener {
                    Log.e("geofence", it.message.toString())
                    if (!locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)!!) {
                        Toast.makeText(
                            this@MainActivity, "GPS provider not available",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (!locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)!!) {
                        Toast.makeText(
                            this@MainActivity, "Network provider not available",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(listOf(geofence))
        }.build()
    }

    fun buildGeofence(latitude : Double, longitude : Double) : Geofence? {
        if(geofence != null) {
            Toast.makeText(this@MainActivity, "stopping Geofence",
                Toast.LENGTH_SHORT).show()
            stopGeofences()
        }
        var newGeofence : Geofence? =  Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(GEOFENCE_REQ_ID)

                // Set the circular region of this geofence.
                .setCircularRegion(
                    latitude,
                    longitude,
                    GEOFENCE_RADIUS
                )

                // Set the expiration duration of the geofence. This geofence gets automatically
                // removed after this period of time.
                .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                // Set the transition types of interest. Alerts are only generated for these
                // transition. We track entry and exit transitions in this sample.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)

                // Create the geofence.
                .build()

        return newGeofence
    }

    fun stopGeofences() {
        geofencingClient?.removeGeofences(mutableListOf<String>(GEOFENCE_REQ_ID))?.run {
            addOnSuccessListener {
                Toast.makeText(this@MainActivity, "Stopping Geofence!",
                    Toast.LENGTH_SHORT).show()
            }
            addOnFailureListener {
                Toast.makeText(this@MainActivity, "Stopping Geofence FAILED: " + it.message,
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    fun requestPermissions() {
        Log.e(TAG, "requesting permissions")
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                android.Manifest.permission.INTERNET
            ),
            PERMISSION_ID
        )

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG, "Permissions Granted")
                // You can use the API that requires the permission.
            }
            else -> {

                Log.d(TAG, "Permissions Not Granted. Need more info.")
            }
        }

        getPermission()
    }

    fun getPermission(): Boolean {
        coarseLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        fineLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        backgroundLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        internet = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.INTERNET
        ) == PackageManager.PERMISSION_GRANTED

        Log.e(TAG, "coarse $coarseLocation : fine $fineLocation : internet $internet : background $backgroundLocation")
        if (!coarseLocation || !fineLocation || !internet || !backgroundLocation) {
            requestPermissions()
        }
        return fineLocation && coarseLocation && internet && backgroundLocation
    }
}

private val TAG: String = "Main Activity"
private const val GEO_DURATION = (60 * 60 * 1000).toLong()
private const val GEOFENCE_REQ_ID = "hearo_geolocation"
private const val GEOFENCE_RADIUS = 500.0f // in meters
private const val GEOFENCE_EXPIRATION_IN_MILLISECONDS = (60 * 5 * 1000).toLong()
private val GEOFENCE_REQ_CODE = 0