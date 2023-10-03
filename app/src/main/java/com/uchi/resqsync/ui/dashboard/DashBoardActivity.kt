package com.uchi.resqsync.ui.dashboard

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.uchi.resqsync.R
import com.uchi.resqsync.utils.Permission

class DashBoardActivity : AppCompatActivity() {
    private lateinit var bottomNavigation : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        loadFragment(LocationMapFragment())
        bottomNavigation = findViewById(R.id.bottomNav)

//        if(!Permission.canUseLocation(this)){
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
//                REQUEST_LOCATION_PERMISSION
//            )
//        }
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
                    loadFragment(AccountFragment())
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

    override fun onResume() {
        super.onResume()
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.layout_container,fragment)
        transaction.commit()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
               // startService(service)
                Toast.makeText(this, "location granted", Toast.LENGTH_SHORT).show()

            }
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

}