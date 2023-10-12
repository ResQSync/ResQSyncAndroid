package com.uchi.resqsync.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.firestore.GeoPoint
import com.uchi.resqsync.broadcast.LocationBroadcastReceiver
import com.uchi.resqsync.models.UserLocation
import com.uchi.resqsync.models.UserModel
import com.uchi.resqsync.utils.FirebaseUtils
import com.uchi.resqsync.utils.PrefConstant
import timber.log.Timber


class LocationService : Service() {
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    val CHANNEL_ID = "ResQSync_Channel_1"
    val CHANNEL_NAME="LocationChannel"

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
            channel
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("ResQSync Location")
            .setAutoCancel(false)
            .setContentText("").build()
        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        location
        return START_STICKY}

    private val location: Unit
        private get() {
            // ---------------------------------- LocationRequest ------------------------------------//
            val mLocationRequestHighAccuracy = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(FASTEST_INTERVAL)
                .setMaxUpdateDelayMillis(LONGEST_WAIT_TIME)
                .build()

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                stopSelf()
                return
            }

            mFusedLocationClient!!.requestLocationUpdates(
                mLocationRequestHighAccuracy, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {

                        val location = locationResult.lastLocation
                        if (location != null) {
                            val user: UserModel = PrefConstant.getUserDetails(applicationContext)
                            val geoPoint = GeoPoint(location.latitude, location.longitude)
                            val userLocation = UserLocation( geoPoint, null,user)
                            saveUserLocation(userLocation)
                        }
                    }
                },
                Looper.myLooper()
            )
        }

    private fun saveUserLocation(userLocation: UserLocation) {
        try {
            FirebaseUtils().currentUserLocationDetails().set(
                userLocation
            ).addOnCompleteListener {task->
                if(task.isSuccessful){
                    Timber.d("Inserted location to DB")
                }
            }
        } catch (e: NullPointerException) {
            Timber.d("saveUserLocation: User instance is null, stopping location service.")
            Timber.e("saveUserLocation: NullPointerException: " + e.message)
            stopSelf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val broadcastIntent = Intent()
        broadcastIntent.action = "locationservice"
        broadcastIntent.setClass(this, LocationBroadcastReceiver::class.java)
        this.sendBroadcast(broadcastIntent)
    }

    companion object {
        private const val UPDATE_INTERVAL = (10 * 1000).toLong()
        private const val FASTEST_INTERVAL: Long = 2000
        private const val LONGEST_WAIT_TIME :Long=20000
    }
}