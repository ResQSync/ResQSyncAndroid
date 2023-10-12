package com.uchi.resqsync.models

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp
import com.google.type.LatLng
import java.util.Date


data class UserLocation(
    val geoPoint: GeoPoint,
    @ServerTimestamp
    val timestamp: Date?,
    val user:UserModel
    ){
    constructor() : this(GeoPoint(0.0, 0.0), null, UserModel())
}

