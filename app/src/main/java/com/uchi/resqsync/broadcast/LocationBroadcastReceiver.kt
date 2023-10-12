package com.uchi.resqsync.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.uchi.resqsync.services.LocationService


class LocationBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
        context?.startForegroundService(Intent(context,LocationService::class.java))
    }
}