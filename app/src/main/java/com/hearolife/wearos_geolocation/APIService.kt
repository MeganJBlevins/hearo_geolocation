package com.hearolife.wearos_geolocation

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.time.LocalDateTime


private const val TAG : String = "APIService"
private const val APIUrl : String = "https://eo1e9cu5vornd7i.m.pipedream.net"

class APIService() {

    fun sendPost(context: Context, latitude: String, longitude: String) {
        Log.e(TAG, "Sending Post $longitude")
        val queue = Volley.newRequestQueue(context)

        val sr: StringRequest = object : StringRequest(
            Method.POST, APIUrl,
            Response.Listener<String?> { response -> Log.e("HttpClient", "success! response: $response") },
            Response.ErrorListener { error -> Log.e("HttpClient", "error: $error") }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["latitude"] = latitude
                params["longitude"] = longitude
                params["time"] = LocalDateTime.now().toString()
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/x-www-form-urlencoded"
                return params
            }
        }
        queue.add(sr)
    }
}