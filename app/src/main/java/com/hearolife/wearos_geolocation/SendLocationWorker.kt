package com.hearolife.wearos_geolocation

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

/* Copyright 2020 Google LLC.
   SPDX-License-Identifier: Apache-2.0 */

private const val TAG : String = "Send Location Worker"
private const val APIUrl : String = "https://eo1e9cu5vornd7i.m.pipedream.net"

class SendLocationWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {


        Log.e(TAG, "Doing Work.")
        return Result.success()
    }
}
