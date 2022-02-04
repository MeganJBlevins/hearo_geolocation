package com.hearolife.wearos_geolocation

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

private const val TAG : String = "APIService"
private const val APIUrl : String = "https://eozp0k3i7q3ytf.m.pipedream.net"

class APIService {

    var testing = "true"
    fun sendPostRequest(context : Context, latitude:String, longitude:String) {
        Toast.makeText(context, "sending post request",
            Toast.LENGTH_SHORT).show()
//        Log.e(TAG, "Sending Post Request")
//        var reqParam = URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8")
//        reqParam += "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(longitude, "UTF-8")
//        val mURL = URL(APIUrl)
//
//        with(mURL.openConnection() as HttpURLConnection) {
//            // optional default is GET
//            requestMethod = "POST"
//
//            val wr = OutputStreamWriter(getOutputStream());
//            wr.write(reqParam);
//            wr.flush();
//
//            println("URL : $url")
//            println("Response Code : $responseCode")
//
//            BufferedReader(InputStreamReader(inputStream)).use {
//                val response = StringBuffer()
//
//                var inputLine = it.readLine()
//                while (inputLine != null) {
//                    response.append(inputLine)
//                    inputLine = it.readLine()
//                }
//                println("Response : $response")
//            }
//        }
    }
}