package com.mabdigital.offers.presentaton.feature.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mabdigital.core.base.model.NotificationModel
import com.mabdigital.core.base.notification.NOTIFICATION_DATA
import com.mabdigital.core.tools.extentions.finishHoleApp
import com.mabdigital.offers.R
import com.mabdigital.offers.databinding.MapLoadViewBinding
import com.mabdigital.offers.domain.feature.map.MapActionState
import com.mabdigital.offers.domain.model.map.PointDetails
import com.mabdigital.offers.presentaton.feature.activity.OfferShareViewModel
import com.mabdigital.offers.tools.*
import com.mabdigital.offers.tools.locationpermission.LocationPermissionHelper
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.lang.ref.WeakReference

class MapFragment : Fragment() {
    private val mViewModel by sharedViewModel<OfferShareViewModel>()
    private var userLocationPoint: Point? = null

    private val listData = mutableListOf<PointDetails>()

    private lateinit var locationPermissionHelper: LocationPermissionHelper
    private val onIndicatorBearingChangedListener by lazy {
        OnIndicatorBearingChangedListener {
            mapView.indicatorBearingChangedListener()
        }
    }
    private val onIndicatorPositionChangedListener by lazy {
        OnIndicatorPositionChangedListener {
            userLocationPoint = it
            mapView.onLocationChangeConfig(it)
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
    private val mapView by lazy { binding.mapView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finishHoleApp()
    }

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
        readNotificationData {
            convertNotificationToPointDetails(it)
        }
        startMapAfterCheckPermissions()
        setupViewModelObserver()
    }

    private inline fun readNotificationData(onNotificationDataFound: (NotificationModel?) -> Unit) {
        requireActivity().intent?.extras?.run {
            val notificationModel = getParcelable<NotificationModel>(NOTIFICATION_DATA)
            onNotificationDataFound(notificationModel)
        }
    }

    private fun convertNotificationToPointDetails(notificationModel: NotificationModel?) {
        listData.clear()
        notificationModel.getPointListModel {
            if (it.isNotEmpty()) {
                listData.addAll(it)
                mapView.printPoint(listData)
                loadDetails(notificationModel?.price ?: "0")
            }
        }
    }

    private fun loadDetails(price: String) {
        val direction = MapFragmentDirections.actionMapFragmentToOfferDetailsFragment(
            listData.toTypedArray(), price
        )
        findNavController().navigate(direction)
    }

    private fun setupViewModelObserver() {
        mViewModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                is MapActionState.MoveToPoint -> moveCamera(it.locationDetails.point)
                is MapActionState.MoveToUser -> userLocationPoint?.let { lastLocation ->
                    moveCamera(
                        lastLocation,
                        13.0
                    )
                }
            }
        }
    }

    private fun moveCamera(point: Point, zoomV: Double = 16.0) {
        onCameraTrackingDismissed()
        mapView.moveCameraToPoint(point, zoomV)
    }

    private fun startMapAfterCheckPermissions() {
        locationPermissionHelper = LocationPermissionHelper(WeakReference(requireActivity()))
        locationPermissionHelper.checkPermissions {
            onMapReady()
        }
    }

    private fun onMapReady() {
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(13.0)
                .build()
        )
        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
            initLocationComponent()
            setupGesturesListener()
        }
        mapView.initOnPointClick()
    }

    private fun initLocationComponent() {
        val locationComponentPlugin = mapView.location
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
        locationComponentPlugin.addOnIndicatorPositionChangedListener(
            onIndicatorPositionChangedListener
        )
        locationComponentPlugin.addOnIndicatorBearingChangedListener(
            onIndicatorBearingChangedListener
        )

    }

    private fun setupGesturesListener() {
        mapView.gestures.addOnMoveListener(onMoveListener)
    }

    private fun onCameraTrackingDismissed() {
        with(mapView) {
            location.removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
            location.removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
            gestures.removeOnMoveListener(onMoveListener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onCameraTrackingDismissed()
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