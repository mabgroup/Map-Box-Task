package com.mabdigital.offers.presentaton.feature.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.mabdigital.offers.R
import com.mabdigital.offers.databinding.MapLoadViewBinding
import com.mabdigital.offers.domain.model.map.PointDetails
import com.mabdigital.offers.domain.model.map.TerminalLocationTypeEnum
import com.mabdigital.offers.tools.locationpermission.LocationPermissionHelper
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import java.lang.ref.WeakReference

class MapFragment : Fragment() {

    private lateinit var locationPermissionHelper: LocationPermissionHelper
    private val onIndicatorBearingChangedListener by lazy {
        OnIndicatorBearingChangedListener {
            binding.mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
        }
    }
    private val onIndicatorPositionChangedListener by lazy {
        OnIndicatorPositionChangedListener {
            binding.mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
            binding.mapView.gestures.focalPoint = binding.mapView.getMapboxMap().pixelForCoordinate(it)
        }
    }

    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }

    private var _binding: MapLoadViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        if (_binding == null)
            _binding = MapLoadViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startMapAfterCheckPermissions()
    }

    private fun startMapAfterCheckPermissions() {
        locationPermissionHelper = LocationPermissionHelper(WeakReference(requireActivity()))
        locationPermissionHelper.checkPermissions {
            onMapReady()
        }
    }

    private fun onMapReady() {
        binding.mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )
        binding.mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
            initLocationComponent()
            setupGesturesListener()
            printPoint(binding.mapView,
                mutableListOf(
                PointDetails(Point.fromLngLat(51.3266,35.7302),TerminalLocationTypeEnum.Source),
                PointDetails(Point.fromLngLat(51.3175,35.7349),TerminalLocationTypeEnum.Destination),
                PointDetails(Point.fromLngLat(51.3133,35.7304),TerminalLocationTypeEnum.Destination),
                PointDetails(Point.fromLngLat(51.3211,35.7324),TerminalLocationTypeEnum.Destination),
            ))
        }
    }

    private fun initLocationComponent() {
        val locationComponentPlugin = binding.mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = true
            this.locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_motorcycle,
                ),
                shadowImage = AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.mapbox_user_icon_shadow,
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(0.6)
                    }
                    stop {
                        literal(20.0)
                        literal(1.0)
                    }
                }.toJson()
            )
        }
        locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)

    }
    private fun setupGesturesListener() {
        binding.mapView.gestures.addOnMoveListener(onMoveListener)
    }
    private fun onCameraTrackingDismissed() {
        binding.mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        binding.mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        binding.mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        binding.mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        binding.mapView.gestures.removeOnMoveListener(onMoveListener)
        _binding = null
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}