package com.uchi.resqsync.utils.map

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.uchi.resqsync.R
import com.uchi.resqsync.models.PeopleClusterMarker


class PeopleClusterManagerRenderer(
    context: Context,
    map: GoogleMap?,
    clusterManager: ClusterManager<PeopleClusterMarker>
) : DefaultClusterRenderer<PeopleClusterMarker>(context, map, clusterManager) {

    private val iconGenerator: IconGenerator
    private val imageView:ImageView
    private val markerWidth:Int
    private val markerHeight:Int

    /**
     * Rendering of the individual ClusterItems
     * @param item
     * @param markerOptions
     */
    override fun onBeforeClusterItemRendered(item: PeopleClusterMarker, markerOptions: MarkerOptions) {
        imageView.setImageResource(item.getIconPicture())
        val icon = iconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.title)
    }

    override fun shouldRenderAsCluster(cluster: Cluster<PeopleClusterMarker>): Boolean {
        return false
    }

    init {
        iconGenerator =IconGenerator(context.applicationContext);
        imageView = ImageView(context.applicationContext)
        markerWidth = context.resources.getDimension(R.dimen.custom_marker_image).toInt()
        markerHeight = context.resources.getDimension(R.dimen.custom_marker_image).toInt()
        imageView.layoutParams = ViewGroup.LayoutParams(markerWidth, markerHeight)
        val padding = context.resources.getDimension(R.dimen.custom_marker_padding).toInt()
        imageView.setPadding(padding, padding, padding, padding)
        iconGenerator.setContentView(imageView)

    }

}