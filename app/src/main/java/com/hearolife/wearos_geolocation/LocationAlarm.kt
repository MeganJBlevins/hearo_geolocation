package com.hearolife.wearos_geolocation

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.CanceledException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.util.Log
import com.google.android.gms.location.LocationServices

private const val TAG = "Location Alarm"
class LocationAlarm : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        getLocation(context)

        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, LocationAlarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 42,
            intent, PendingIntent.FLAG_UPDATE_CURRENT)

        manager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            1000 * 30,
            pendingIntent)
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(context: Context) {
        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                Log.e(TAG, "Alarm Location : ${location?.latitude} : ${location?.longitude}")
                // Got last known location. In some rare situations this can be null.
            }
    }
}