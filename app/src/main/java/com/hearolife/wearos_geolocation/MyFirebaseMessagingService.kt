package com.hearolife.wearos_geolocation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder



private const val TAG : String = "MyFirebaseMessagingService"
const val channelId = "notification_channel"
const val channelName = "com.hearolife.wearos_location"

class MyFirebaseMessagingService : FirebaseMessagingService(){

    // this is for firebase messages
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Toast.makeText(this, "Notification Received: " + remoteMessage.notification!!.title!!,
            Toast.LENGTH_SHORT).show()
//        Log.d(TAG, remoteMessage.notification!!.title!!)
        if(remoteMessage.getNotification() != null) {
//            sendPostRequest("34.789", "-91.123")
            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }
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
        val remoteView = RemoteViews("com.hearolife.wearos_location", R.layout.notification)

        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.app_logo, R.drawable.hi_green)
        Log.d("Push Notifications: ", "getting remote view: $title")
        return remoteView
    }
}