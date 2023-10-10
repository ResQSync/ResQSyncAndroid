package com.uchi.resqsync.ui.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.toObject
import com.uchi.resqsync.R
import com.uchi.resqsync.models.UserLocation
import com.uchi.resqsync.models.UserModel
import com.uchi.resqsync.utils.FirebaseUtils
import com.uchi.resqsync.utils.PrefConstant
import timber.log.Timber

class LocationMapFragment : Fragment() {
    private lateinit var mapView: MapView
    private var map: GoogleMap? = null
    private lateinit var lastGeoPoint: GeoPoint
    private lateinit var userModel: UserModel
    private lateinit var userLocations:ArrayList<UserLocation>
    private lateinit var mapBoundary:LatLngBounds
    private lateinit var userPosition:UserLocation

    //get last known location
    private lateinit var fusedLastLocation:FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)

        fusedLastLocation=LocationServices.getFusedLocationProviderClient(requireActivity())
        getUserDetails()
        setUserLocation()






//
//        val zoomLevel = 15f
//        val latitude = 37.422160
//        val longitude = -122.084270
//        pinLocationOnMap(LatLng(37.422161, -122.084260),"test")
//        mapView.getMapAsync {
//            it.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), zoomLevel))
//            it.addMarker(
//                MarkerOptions()
//                .title("india")
//                .position(LatLng(latitude, longitude))
//            )
//            it.isMyLocationEnabled=true
//            map = it
//        }


    }

    //extra test
    private fun pinLocationOnMap(latLng: LatLng, tag:String){
        mapView.getMapAsync { googleMap ->
            googleMap.addMarker(
                MarkerOptions()
                    .title(tag)
                    .position(latLng)
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            map = googleMap
        }
    }




    private fun getDeviceLastKnownLocation(){
        Timber.d("LastKnownLocation()")
        Timber.i("Checking for location permissions")
        // Required here as a safety check
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PrefConstant.PERMISSIONS_REQUEST_ACCESS_LOCATION
            )
            return
        }
        fusedLastLocation.lastLocation.addOnCompleteListener {task->
            if (task.isSuccessful){
                val location = task.result
                val geoPoint = GeoPoint(location.latitude,location.longitude)
                lastGeoPoint=geoPoint
                saveUserLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
//    private fun setUserLocation(){
//                mapView.getMapAsync {
//            it.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), zoomLevel))
//            it.addMarker(
//                MarkerOptions()
//                .title("india")
//                .position(LatLng(latitude, longitude))
//            )
//            it.isMyLocationEnabled=true
//            map = it
//        }
//    }

    private fun getUserDetails(){
        FirebaseUtils().getUserDetails().document(FirebaseUtils().currentUserId()?:"")
            .get().addOnCompleteListener {task->
                if(task.isSuccessful){
                    val user =task.result.toObject(UserModel::class.java)
                    //getDeviceLastKnownLocation()
                    if(user!=null){
                        userModel = UserModel(user.name,user.email,user.userId)
                    }

                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun setUserLocation(){
                        mapView.getMapAsync {
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(28.71981,47.0869), 15f))
            it.addMarker(
                MarkerOptions()
                .title("india")
                .position(LatLng(28.71981,47.0869))
            )
            it.isMyLocationEnabled=true

            map = it
        }
    }


    private fun saveUserLocation(){
        Timber.d("Saving user's last known location")
        FirebaseUtils().currentUserLocationDetails().set(
            UserLocation(lastGeoPoint,null,userModel)
        ).addOnCompleteListener { task ->
            if(task.isSuccessful){
              Timber.d("successfully updated the location")
            }else{
                Timber.d("Error updating the location")
            }
        }
    }

    private fun getUserLocation(userModel: UserModel){
        FirebaseUtils().currentUserLocationDetails().get().addOnCompleteListener { task->
            if(task.isSuccessful){
                if (task.result.toObject(UserLocation::class.java)!=null){
                    userLocations.add(task.result.toObject(UserLocation::class.java)!!)
                }
            }
        }
    }

    //Required
    private fun setCameraView(latLng: LatLng, zoom : Float){
        Timber.d("Moving Camera")
        val bottomBoundary = userPosition.geoPoint.latitude-.1
        val leftBoundary = userPosition.geoPoint.longitude-.1
        val topBoundary = userPosition.geoPoint.latitude+.1
        val rightBoundary = userPosition.geoPoint.longitude+.1
        mapBoundary = LatLngBounds(LatLng(bottomBoundary,leftBoundary),LatLng(topBoundary,rightBoundary))

        map?.moveCamera(CameraUpdateFactory.newLatLngBounds(mapBoundary,0))
        val options = MarkerOptions()
            .position(latLng)
            .title("my")
        map?.addMarker(options)

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

}