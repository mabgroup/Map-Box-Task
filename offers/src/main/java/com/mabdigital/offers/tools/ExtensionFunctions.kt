package com.mabdigital.offers.tools.locationpermission

import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener

fun MapView.indicatorBearingChangedListener() =  OnIndicatorBearingChangedListener {
    this.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
}

fun MapView.onIndicatorPositionChangedListener() = OnIndicatorPositionChangedListener {
    this.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
    this.gestures.focalPoint = this.getMapboxMap().pixelForCoordinate(it)
}