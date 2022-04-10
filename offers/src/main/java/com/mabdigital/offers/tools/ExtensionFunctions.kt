package com.mabdigital.offers.tools

import com.mabdigital.core.base.model.NotificationModel
import com.mabdigital.offers.domain.model.map.PointDetails
import com.mabdigital.offers.domain.model.map.TerminalLocationTypeEnum
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener

fun MapView.indicatorBearingChangedListener() = OnIndicatorBearingChangedListener {
    this.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
}

fun MapView.onLocationChangeConfig(point: Point) {
    this.getMapboxMap().setCamera(CameraOptions.Builder().center(point).build())
    this.gestures.focalPoint = this.getMapboxMap().pixelForCoordinate(point)
}

fun MapView.moveCameraToPoint(point: Point, zoomV:Double=16.0) {
    val cameraPosition = CameraOptions.Builder()
        .zoom(zoomV)
        .center(point)
        .build()
    this.getMapboxMap().setCamera(
        cameraPosition
    )
}

fun MapView.initOnPointClick() {
    val pointAnnotationManager = this.annotations.createPointAnnotationManager()
    pointAnnotationManager.apply {
        addClickListener(
            OnPointAnnotationClickListener {
                val cameraPosition = CameraOptions.Builder()
                    .zoom(14.0)
                    .center(it.point)
                    .build()
                this@initOnPointClick.getMapboxMap().setCamera(cameraPosition)
                true
            }
        )
    }
}

fun NotificationModel?.getPointListModel(onDone: (MutableList<PointDetails>) -> Unit) {
    this?.let {
        val listData = mutableListOf<PointDetails>()
        it.source?.run {
            listData.add(
                0,
                PointDetails(
                    Point.fromLngLat(Longitude, Latitude),
                    address ?: "",
                    TerminalLocationTypeEnum.toType(type ?: TerminalLocationTypeEnum.Unknown.name)
                )
            )
        }
        it.array.forEach { data ->
            listData.add(
                PointDetails(
                    Point.fromLngLat(data.Longitude, data.Latitude),
                    data.address ?: "",
                    TerminalLocationTypeEnum.toType(
                        data.type ?: TerminalLocationTypeEnum.Unknown.name
                    )
                )
            )
        }
        onDone(listData)
    } ?: onDone(mutableListOf())
}