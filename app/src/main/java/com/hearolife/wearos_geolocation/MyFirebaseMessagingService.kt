package com.hearolife.wearos_geolocation

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


private const val locationId = "12345"
private const val individualId = "67890"
private const val TAG : String = "MyFirebaseMessagingService"
const val channelId = "notification_channel"
const val channelName = "com.hearolife.wearos_location"

class MyFirebaseMessagingService : FirebaseMessagingService(){
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "On message Received.")
        val data = remoteMessage.data
        val location_id = data["location_id"]
        val individual_id = data["individual_id"]
        val task = data["task"]

        if(locationId == location_id && individual_id == individualId) {
            if (task == "get_location") {
                var locationService = LocationService()
                locationService.getLastLocation(applicationContext, "sendToAPI")
            }
        }
        generateNotification("Received Notification", "Task: $task")
    }

    fun getToken(context: Context) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            Log.e(TAG, token)
            Toast.makeText(context, token , Toast.LENGTH_SHORT).show()
        })
    }

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token:String) {
        Log.e(TAG, token)
    }

    fun generateNotification(title: String, message: String){
        Log.d("Push Notification: ", "Generating Notification $title")

        val intent = Intent(this, MainActivity::class.java).apply{}
        // clears activities in stack and puts this at top
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        // channel id, channel name
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(false)
            .setContentIntent(pendingIntent)

        // attach builder to layout
        builder = builder.setContent(getRemoteView(title, message))

        // create notification manager
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("Push Notifications: ", "Build version needs channel added")
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Hearo Notification"
            Log.d("Push Notifications: ", "notification channel title: ${notificationChannel.description}")

            notificationManager.createNotificationChannel(notificationChannel)
        }
        Log.d("Push Notifications: ", "notifying")


        notificationManager.notify(0, builder.build())

    }

    private fun getRemoteView(title: String, message: String): RemoteViews? {
        val remoteView = RemoteViews("com.hearolife.wearos_geolocation", R.layout.notification)

        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.app_logo, R.drawable.hi_green)
        Log.d("Push Notifications: ", "getting remote view: $title")
        return remoteView
    }

}