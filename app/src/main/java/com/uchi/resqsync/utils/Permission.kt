package com.uchi.resqsync.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object Permission {
    const val REQUEST_LOCATION_PERMISSION = 1

    fun canUseLocation(context: Context): Boolean {
        return hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun canUseBackgroundLocation(context: Context): Boolean {
        return hasPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun hasPermission(context: Context, accessFineLocation: String): Boolean {
        return ContextCompat.checkSelfPermission(context, accessFineLocation) == PackageManager.PERMISSION_GRANTED
    }
}