/*
 *  Copyright (c) 2023 Ashish Yadav <mailtoashish693@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.uchi.resqsync.ui.dashboard

import android.Manifest
import android.app.ActivityManager
import android.app.Dialog
import android.app.usage.UsageEvents.Event
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.uchi.resqsync.R
import com.uchi.resqsync.models.UserCircleModel
import com.uchi.resqsync.models.UserLocation
import com.uchi.resqsync.services.LocationService
import com.uchi.resqsync.snackbar.BaseSnackbarBuilderProvider
import com.uchi.resqsync.snackbar.SnackbarBuilder
import com.uchi.resqsync.snackbar.showSnackbar
import com.uchi.resqsync.utils.FirebaseUtils
import com.uchi.resqsync.utils.Permission
import com.uchi.resqsync.utils.PrefConstant
import com.uchi.resqsync.utils.PrefConstant.ERROR_DIALOG_REQUEST
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber


class DashBoardActivity : AppCompatActivity(), BaseSnackbarBuilderProvider{
    private lateinit var bottomNavigation : BottomNavigationView

    override val baseSnackbarBuilder: SnackbarBuilder = {
        anchorView = findViewById<BottomNavigationView>(R.id.bottomNav)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        setFamilyDetails()
        loadFragment(LocationMapFragment())
        bottomNavigation = findViewById(R.id.bottomNav)

        // new and this is required as we suppressed permission check in LocationFragment
        mapPermissionCheck()
        checkPermissions()

        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    loadFragment(LocationMapFragment())
                    true
                }
                R.id.action_sos -> {
                    loadFragment(EmergencyFragment())
                    true
                }
                R.id.action_account -> {
                    loadFragment(ContactFragment())
                    true
                }
                R.id.action_settings -> {
                    loadFragment(SettingsFragment())
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
//        if(checkMapServices()){
//             if(checkMapServices()){
//
//                    }
//        }
//    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.layout_container,fragment)
        transaction.commit()
    }

    private fun checkPermissions() {
        if(!Permission.canUseLocation(this) && !Permission.canUseBackgroundLocation(this)) {
           locationPermissions.launch(
               arrayOf(
                   Manifest.permission.ACCESS_FINE_LOCATION,
                   Manifest.permission.ACCESS_COARSE_LOCATION
               )
           )
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                   if (Permission.canUseBackgroundLocation(this)) {
                       backgroundLocation.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                   }
               }

       }else{
           Toast.makeText(this, "location granted", Toast.LENGTH_SHORT).show()

       }
    }

//    private val locationPermissions =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
//            when {
//                it.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                        if (Permission.canUseBackgroundLocation(this)) {
//                            backgroundLocation.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//                        }
//                    }
//                }
//                it.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
//
//                }
//            }
//        }

    private val backgroundLocation =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                // Background location permission granted.
                // You can start your service here.
                // startService(service)
            } else {
                Toast.makeText(this, "grant permission", Toast.LENGTH_SHORT).show()
            }
        }

    private fun showPermissionDeniedDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permission Required")
            .setMessage("This app requires location permission to function properly.")
            .setPositiveButton("Grant Permission") { _, _ ->
                // Launch permission request again
                checkPermissions()
            }
            .setNegativeButton("Exit App") { _, _ ->
                finish() // Close the app
            }
            .setCancelable(false) // User can't dismiss the dialog without choosing an option
        val dialog = builder.create()
        dialog.show()
    }

    private fun handlePermissionResults(permissions: Map<String, Boolean>) {
        if (permissions.all { !it.value }) {
            showPermissionDeniedDialog()
        } else {
            // Permissions granted, continue with your logic
            Toast.makeText(this, "Location permissions granted", Toast.LENGTH_SHORT).show()
        }
    }

    private val locationPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            permissions -> handlePermissionResults(permissions)
    }

    fun isServicesOK(): Boolean {
        Timber.d( "isServicesOK: checking google services version")
        val available =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this@DashBoardActivity)
        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Timber.d( "isServicesOK: Google Play Services is working")
            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred but we can resolve it
            Timber.d( "isServicesOK: an error occurred but we can fix it")
            val dialog: Dialog? = GoogleApiAvailability.getInstance()
                .getErrorDialog(this@DashBoardActivity, available, ERROR_DIALOG_REQUEST)
            dialog?.show()
        } else {
            showSnackbar(getString(R.string.cant_make_map_requests))
        }
        return false
    }

    private fun checkMapServices(): Boolean {
        if (isServicesOK()) {
            return true
//            if (isMapsEnabled()) {
//                return true
//            }
        }
        return false
    }


    fun setFamilyDetails(){
        FirebaseUtils().userCircleDetails("family").get()
            .addOnCompleteListener {task->
                if(task.isSuccessful){
                    val details = task.result.toObject(UserCircleModel::class.java)
                    if (details != null) {
                        PrefConstant.saveMembersDetails(this@DashBoardActivity,"family",details)
                    }
                }
            }
    }

    fun mapPermissionCheck(){
        // Required here as a safety check
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PrefConstant.PERMISSIONS_REQUEST_ACCESS_LOCATION
            )
            return
        }
    }

//     fun startLocationService() {
//        if (!isLocationServiceRunning()) {
//            val serviceIntent = Intent(this, LocationService::class.java)
//            //        this.startService(serviceIntent);
//
//            this@DashBoardActivity.startForegroundService(serviceIntent)
//        }
//    }
//
//    private fun isLocationServiceRunning(): Boolean {
//        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
//        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
//            if ("com.uchi.resqsync.services.LocationService" == service.service.className) {
//                Timber.d("isLocationServiceRunning: location service is already running.")
//                return true
//            }
//        }
//        Timber.d("isLocationServiceRunning: location service is not running.")
//        return false
//    }

    override fun onStart() {
        super.onStart()
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    @Subscribe
    fun receiveLocationEvent(locationEvent: UserLocation){
        Timber.d(locationEvent.geoPoint.toString())
    }

}