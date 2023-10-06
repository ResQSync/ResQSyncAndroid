package com.uchi.resqsync.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.uchi.resqsync.utils.PrefConstant.PERMISSIONS_REQUEST_ACCESS_LOCATION

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

    fun permissionCheckFineCoarse(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSIONS_REQUEST_ACCESS_LOCATION
            )
            return
        }
    }

}