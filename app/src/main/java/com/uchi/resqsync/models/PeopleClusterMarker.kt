package com.uchi.resqsync.models

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.auth.User
import com.google.maps.android.clustering.ClusterItem


 class  PeopleClusterMarker(
    lat: Double,
    lng: Double,
    title: String,
    snippet: String,
     iconPicture:Int,
     user:UserModel
) : ClusterItem {

    private val position: LatLng
    private val title: String
    private val snippet: String
    private val iconPicture:Int
    private val user:UserModel


     override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String {
        return title
    }

    override fun getSnippet(): String {
        return snippet
    }

    override fun getZIndex(): Float {
        return 0f
    }

     fun getIconPicture():Int{
         return iconPicture
     }

     fun getUser():UserModel{
         return user
     }

    init {
        position = LatLng(lat, lng)
        this.title = title
        this.snippet = snippet
        this.iconPicture = iconPicture
        this.user =user
    }
}



