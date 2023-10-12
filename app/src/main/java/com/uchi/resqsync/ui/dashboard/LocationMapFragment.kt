package com.uchi.resqsync.ui.dashboard

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.clustering.ClusterManager
import com.uchi.resqsync.R
import com.uchi.resqsync.broadcast.LocationBroadcastReceiver
import com.uchi.resqsync.models.PeopleClusterMarker
import com.uchi.resqsync.models.UserLocation
import com.uchi.resqsync.models.UserModel
import com.uchi.resqsync.services.LocationService
import com.uchi.resqsync.utils.FirebaseUtils
import com.uchi.resqsync.utils.PrefConstant
import com.uchi.resqsync.utils.map.PeopleClusterManagerRenderer
import timber.log.Timber


class LocationMapFragment : Fragment() {
    private lateinit var mapView: MapView
    private var map: GoogleMap? = null
    private lateinit var lastGeoPoint: GeoPoint
    private lateinit var userModel: UserModel
    private lateinit var userLocations:MutableList<UserLocation>
    private lateinit var mapBoundary:LatLngBounds
    private lateinit var userPosition:UserLocation
    //get last known location
    private lateinit var fusedLastLocation:FusedLocationProviderClient
    private lateinit var clusterManager: ClusterManager<PeopleClusterMarker>
    private lateinit var clusterMarkers: ArrayList<PeopleClusterMarker>
    private var clusterManagerRenderer: PeopleClusterManagerRenderer? = null


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

        userLocations = mutableListOf()
        fusedLastLocation=LocationServices.getFusedLocationProviderClient(requireActivity())


        loadFamilyMembers()
        setMyLocation()
        //
        getUserDetails()



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

    private fun getUserDetails(){
        FirebaseUtils().getUserDetails().document(FirebaseUtils().currentUserId()?:"")
            .get().addOnCompleteListener {task->
                if(task.isSuccessful){
                    val user =task.result.toObject(UserModel::class.java)
                    getDeviceLastKnownLocation()
                    if(user!=null){
                        userModel = UserModel(user.name,user.email,user.userId)
                    }
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLastKnownLocation(){
        Timber.d("LastKnownLocation()")
        Timber.i("Checking for location permissions")
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
    private fun setMyLocation() {
        mapView.getMapAsync {
            it.isMyLocationEnabled = true
            map = it
            fusedLastLocation.lastLocation.addOnCompleteListener {task->
                val location = task.result
                val geoPoint = GeoPoint(location.latitude,location.longitude)
                userPosition= UserLocation(geoPoint, null,PrefConstant.getUserDetails(requireContext()))

            }
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

    fun loadFamilyMembers() {
        val members = PrefConstant.getMembersDetails(requireContext(), "family")
        if (members != null) {
            val membersCount = members.familyMembers.size
            var loadedMembers = 0

            for (member in members.familyMembers) {
                getUserLocation(member) { userLocation ->
                    // Handle user location data here
                    if (userLocation != null) {
                        userLocations.add(userLocation)
                    }

                    loadedMembers++
                    if (loadedMembers == membersCount) {
                        addMapMarkers()
                    }
                }
            }

        }
    }

    private fun getUserLocation(userModel: UserModel, callback: (UserLocation?) -> Unit) {
        FirebaseUtils().memberLocationDetails(userModel.userId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userLocation = task.result.toObject(UserLocation::class.java)
                callback(userLocation)
            } else {
                callback(null)
            }
        }
        startLocationService()
    }


    //Required
    private fun setCameraView(){
        Timber.d("Moving Camera")
        val bottomBoundary = userPosition.geoPoint.latitude-.1
        val leftBoundary = userPosition.geoPoint.longitude-.1
        val topBoundary = userPosition.geoPoint.latitude+.1
        val rightBoundary = userPosition.geoPoint.longitude+.1
        mapBoundary = LatLngBounds(LatLng(bottomBoundary,leftBoundary),LatLng(topBoundary,rightBoundary))
        map?.moveCamera(CameraUpdateFactory.newLatLngBounds(mapBoundary,0))

    }

    private fun addMapMarkers() {
        if (map != null) {
                clusterManager =
                    ClusterManager<PeopleClusterMarker>(requireActivity().applicationContext, map)
            Timber.e("Entered 3")
            if (clusterManagerRenderer == null) {
                clusterManagerRenderer = PeopleClusterManagerRenderer(
                    requireActivity(),
                    map,
                    clusterManager
                )
                clusterManager.renderer = clusterManagerRenderer
            }
            Timber.e("Entered userLocation $userLocations")
            for (userLocation in userLocations) {
                Timber.e("Entered 4")
                Timber.d( "addMapMarkers: location: " + userLocation.geoPoint.toString())
                try {
                    var snippet = ""
                    snippet =
                        if (userLocation.user.userId == FirebaseAuth.getInstance().uid) {
                            "This is you"
                        } else {
                            "Determine route to " + userLocation.user.name + "?"
                        }
                    var avatar: Int = com.uchi.resqsync.R.drawable.account // set the default avatar
                    try {
                        // TODO add this picture field in userModel
                      //  avatar = userLocation.user.getAvatar().toInt()
                    } catch (e: NumberFormatException) {
                        Timber.d(
                            "addMapMarkers: no avatar for " + userLocation.user.name + ", setting default."
                        )
                    }
                    val newClusterMarker = PeopleClusterMarker(
                            userLocation.geoPoint.latitude,
                            userLocation.geoPoint.longitude,
                        userLocation.user.name,
                        snippet,
                        avatar,
                        userLocation.user
                    )
                    Timber.e("Entered 5")
                    clusterManager.addItem(newClusterMarker)
                    clusterMarkers = ArrayList()
                    clusterMarkers.add(newClusterMarker)
                } catch (e: NullPointerException) {
                    Timber.e( "addMapMarkers: NullPointerException: " + e.message)
                }
            }
            clusterManager.cluster()
            setCameraView()
        }
    }

    fun startLocationService() {
        if (!isLocationServiceRunning()) {
            val serviceIntent = Intent(requireContext(), LocationService::class.java)
            //        this.startService(serviceIntent);

            requireActivity().startForegroundService(serviceIntent)
        }
    }

    private fun isLocationServiceRunning(): Boolean {
        val activity = requireActivity()

        val manager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if ("com.uchi.resqsync.services.LocationService" == service.service.className) {
                Timber.d("isLocationServiceRunning: location service is already running.")
                return true
            }
        }
        Timber.d("isLocationServiceRunning: location service is not running.")
        return false
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

    override fun onDestroy() {
        val broadcastIntent = Intent()
        broadcastIntent.action = "locationservice"
        broadcastIntent.setClass(requireActivity().applicationContext, LocationBroadcastReceiver::class.java)
            requireActivity().sendBroadcast(broadcastIntent)
        super.onDestroy()
    }



}