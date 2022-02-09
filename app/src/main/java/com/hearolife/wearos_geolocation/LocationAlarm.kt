package com.hearolife.wearos_geolocation

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.firebase.FirebaseApp


class LocationAlarm : BroadcastReceiver() {
    @SuppressLint("InvalidWakeLockTag")
    override fun onReceive(context: Context, intent: Intent?) {
        FirebaseApp.initializeApp(context);
        Log.e("LOcation Alarm", "alarm running")

        var location = LocationService()
        location.getLastLocation(context, "sendToAPI")

        Handler(Looper.getMainLooper()).postDelayed(
            { setAlarm(context) },
            1000 * 60 * 15
        )

    }

    fun setAlarm(context: Context) {
        Log.e("LOcation Alarm", "Setting Alarm")
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, LocationAlarm::class.java)
        val pi = PendingIntent.getBroadcast(context, 0, i, 0)
        am.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            0,
            pi)
    }

    fun cancelAlarm(context: Context) {
        Log.e("CANCEL ALARM ", "CAncelling alarm")
        val intent = Intent(context, LocationAlarm::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }
}