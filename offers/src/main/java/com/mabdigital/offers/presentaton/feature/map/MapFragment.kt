package com.mabdigital.offers.presentaton.feature.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mabdigital.offers.R
import com.mabdigital.offers.databinding.MapLoadViewBinding
import com.mabdigital.offers.domain.feature.map.MapActionState
import com.mabdigital.offers.domain.model.map.PointDetails
import com.mabdigital.offers.domain.model.map.TerminalLocationTypeEnum
import com.mabdigital.offers.presentaton.feature.activity.OfferShareViewModel
import com.mabdigital.offers.tools.locationpermission.LocationPermissionHelper
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
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
            binding.mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
        }
    }
    private val onIndicatorPositionChangedListener by lazy {
        OnIndicatorPositionChangedListener {
            userLocationPoint = it
            binding.mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
            binding.mapView.gestures.focalPoint =
                binding.mapView.getMapboxMap().pixelForCoordinate(it)
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
        readNotificationData {
            convertNotificationToPointDetails(it)
        }
        startMapAfterCheckPermissions()
        setupViewModelObserver()
    }

    private inline fun readNotificationData(onNotificationDataFound:(NotificationModel?)->Unit) {
        requireActivity().intent?.extras?.run {
            val notificationModel = getParcelable<NotificationModel>(NOTIFICATION_DATA)
            onNotificationDataFound(notificationModel)
        }
    }

    private fun convertNotificationToPointDetails(notificationModel: NotificationModel?) {
        notificationModel?.let {
            it.array.forEach { data ->
                listData.add(
                    PointDetails(
                        Point.fromLngLat(data.Longitude,data.Latitude),
                        data.address,
                        TerminalLocationTypeEnum.toType(data.type)
                    )
                )
            }
            loadDetails(it.price)
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
                is MapActionState.MoveToUser -> userLocationPoint?.let {lastLocation-> moveCamera(lastLocation,11.0) }
            }
        }
    }

    private fun moveCamera(point: Point,zoomV:Double=16.0) {
        onCameraTrackingDismissed()
        val cameraPosition = CameraOptions.Builder()
            .zoom(zoomV)
            .center(point)
            .build()
        binding.mapView.getMapboxMap().setCamera(
            cameraPosition
        )
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
                .zoom(13.0)
                .build()
        )
        binding.mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
            initLocationComponent()
            setupGesturesListener()
            printPoint(binding.mapView, listData)
        }
        initOnPointClick()
    }

    private fun initOnPointClick() {
        val x = binding.mapView.annotations.createPointAnnotationManager()
        x.apply {
            addClickListener(
                OnPointAnnotationClickListener {
                    val cameraPosition = CameraOptions.Builder()
                        .zoom(14.0)
                        .center(it.point)
                        .build()
                    binding.mapView.getMapboxMap().setCamera(cameraPosition)
                    true
                }
            )
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
        locationComponentPlugin.addOnIndicatorPositionChangedListener(
            onIndicatorPositionChangedListener
        )
        locationComponentPlugin.addOnIndicatorBearingChangedListener(
            onIndicatorBearingChangedListener
        )

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

    private fun getBoundArea(pointList: MutableList<Point>): CameraBoundsOptions {
        pointList.sortBy { it.longitude() }
        return CameraBoundsOptions.Builder()
            .bounds(
                CoordinateBounds(
                    pointList.last(),
                    pointList.first(),
                    false
                )
            )
            .build()
    }

    private fun setupBounds(bounds: CameraBoundsOptions) {
        binding.mapView.getMapboxMap().setBounds(bounds)
    }
}