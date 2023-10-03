package com.uchi.resqsync.maps

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class LocationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Fetch location updates here
        // You can also post a notification or broadcast the location
        return Result.success()
    }
}