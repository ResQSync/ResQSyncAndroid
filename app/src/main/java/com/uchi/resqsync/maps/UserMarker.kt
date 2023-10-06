package com.uchi.resqsync.maps

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class UserMarker(
    lat: Double,
    lng: Double,
    title: String,
    snippet: String,
    iconPicture:Int,
) : ClusterItem {

    private val position: LatLng
    private val title: String
    private val snippet: String
    private val iconPicture: Int

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

    init {
        position = LatLng(lat, lng)
        this.title = title
        this.snippet = snippet
        this.iconPicture = iconPicture
    }
}

