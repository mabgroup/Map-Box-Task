package com.mabdigital.offers.presentaton.feature.map

import com.mabdigital.offers.R
import com.mabdigital.offers.databinding.MarkerViewBinding
import com.mabdigital.offers.databinding.SourceViewBinding

import com.mabdigital.offers.domain.model.map.PointDetails
import com.mabdigital.offers.domain.model.map.TerminalLocationTypeEnum
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.viewannotation.viewAnnotationOptions

fun printPoint(
    mapView: MapView,
    dataList: MutableList<PointDetails>
) {
    loadData(mapView,dataList)
}

@OptIn(MapboxExperimental::class)
private fun loadData(mapView: MapView,
                     dataList: MutableList<PointDetails>) {
    val annotationManager = mapView.viewAnnotationManager
    dataList.forEachIndexed { index, pointDetails ->

        if (pointDetails.type == TerminalLocationTypeEnum.Source) {
            val sourceView = annotationManager.addViewAnnotation(
                resId = R.layout.source_view,
                options = viewAnnotationOptions {
                    geometry(pointDetails.point)
                }
            )
            SourceViewBinding.bind(sourceView)
        } else {
            val pointView = annotationManager.addViewAnnotation(
                resId = R.layout.marker_view,
                options = viewAnnotationOptions {
                    geometry(pointDetails.point)
                }
            )
            MarkerViewBinding.bind(pointView).apply {
                destId.text = index.toString()
            }
        }
    }
}