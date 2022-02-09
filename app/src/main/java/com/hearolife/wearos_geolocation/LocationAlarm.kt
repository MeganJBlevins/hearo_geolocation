package com.hearolife.wearos_geolocation

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseApp


class LocationAlarm : BroadcastReceiver() {
    @SuppressLint("InvalidWakeLockTag")
    override fun onReceive(context: Context, intent: Intent?) {
        FirebaseApp.initializeApp(context);

        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LocationAlarm")
        wl.acquire()
        Log.e("LOcation Alarm", "alarm running")

        var location = LocationService()
        location.getLastLocation(context, "sendToAPI")

        setAlarm(context)
        wl.release()
    }

    fun setAlarm(context: Context) {
        Log.e("LOcation Alarm", "Setting Alarm")
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, LocationAlarm::class.java)
        val pi = PendingIntent.getBroadcast(context, 0, i, 0)
        am.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            (1000 * 30).toLong(),
            pi)// Millisec * Second * Minute
    }

    fun cancelAlarm(context: Context) {
        val intent = Intent(context, LocationAlarm::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }
}