package com.hearolife.wearos_geolocation

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import java.util.*


private var TAG : String = "AlarmService"

class AlarmService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "ON START COMMAND")
//       NewLocationData()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.e(TAG, "ON Bind")
        return null
    }

    @SuppressLint("MissingPermission")
    fun NewLocationData(){
//        val locationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)
//
//        locationProviderClient.lastLocation
//            .addOnSuccessListener {
//                sendLocation(it)
//                Log.e(TAG, "Sucdess! New Location retrieved.")
//            };

    }

    private fun sendLocation(location: Location) {
//        var apiService = APIService()
//        apiService.sendPost(
//            applicationContext,
//            location.latitude.toString(),
//            location.longitude.toString()
//        )
//        fusedLocationProviderClient.removeLocationUpdates(removeCallback)
//        setAlarm()
    }

    fun setAlarm(){
//        val launchIntent = Intent(this, AlarmService::class.java)
//
//        Log.e(TAG, "Setting Alarm from Service")
//        val manager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        var c = Calendar.getInstance();
//        c.add(Calendar.SECOND, 30);
//        var afterThirtySeconds = c.timeInMillis;
//
//        manager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            afterThirtySeconds,
//            PendingIntent.getService(this, 0, launchIntent, 0))

    }

    private val sendToAPI = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            Log.e(TAG, "Send to API Callback running")
//            super.onLocationResult(locationResult)
//            var location = locationResult.lastLocation
//            sendLocation(location)
//            Log.d("Debug:", "Your Location: ${location.longitude} : ${location.latitude}")
//        }
    }

    private val removeCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            Log.e(TAG, "Location updates removed.")
//            super.onLocationResult(locationResult)
//
//        }
    }
}