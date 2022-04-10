package com.mabdigital.offers.presentaton.feature.map

import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.mabdigital.offers.R
import com.mabdigital.offers.databinding.MarkerViewBinding

import com.mabdigital.offers.domain.model.map.PointDetails
import com.mabdigital.offers.domain.model.map.TerminalLocationTypeEnum
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.viewannotation.viewAnnotationOptions

fun MapView.printPoint(
    dataList: MutableList<PointDetails>
) {
    loadData(this,dataList)
}

@OptIn(MapboxExperimental::class)
private fun loadData(mapView: MapView,
                     dataList: MutableList<PointDetails>) {
    val annotationManager = mapView.viewAnnotationManager
    dataList.forEachIndexed { index, pointDetails ->
        val pointView = annotationManager.addViewAnnotation(
            resId = R.layout.marker_view,
            options = viewAnnotationOptions {
                geometry(pointDetails.point)
            }
        )
        MarkerViewBinding.bind(pointView).apply {
            if(pointDetails.type == TerminalLocationTypeEnum.Source) {
                destId.visibility = View.GONE
                appCompatImageView.setImageDrawable(ResourcesCompat.getDrawable(root.resources,R.drawable.ic_pin_source,null))
            } else {
                destId.visibility = View.VISIBLE
                destId.text = index.toString()
                appCompatImageView.setImageDrawable(ResourcesCompat.getDrawable(root.resources,R.drawable.ic_map_pin,null))
            }
        }
    }
}