package com.example.unisoldevtestwork.feature_map.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.unisoldevtestwork.R
import com.example.unisoldevtestwork.feature_office.data.model.OfficesItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider

@SuppressLint("VisibleForTests", "MissingPermission")
@Composable
fun MapViewContainer(
    offices: State<List<OfficesItem>>,
    mapView: MapView,
    fusedLocationClient: FusedLocationProviderClient,
    modifier: Modifier = Modifier,
    onSearchOffices: (lat: Double, long: Double, rad: Double) -> Unit,
    showBottomSheet: MutableState<Boolean>
) {
    var latitudeUser by remember {
        mutableDoubleStateOf(0.0)
    }
    var longitudeUser by remember {
        mutableDoubleStateOf(0.0)
    }

    val context = LocalContext.current
    userLocationPoint(context, mapView)
    addOfficeMarkers(mapView, offices.value, context, latitudeUser, longitudeUser)
    AndroidView(
        modifier = modifier,
        factory = { mapView }
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                //onSearchOffices(location.latitude, location.longitude, 10000.0)
                onSearchOffices(55.727469, 37.614716, 12.0)
                mapView.map.move(
                    CameraPosition(
                        Point(location.latitude, location.longitude),
                        //Point(56.727469, 37.614716),
                        17.0f,
                        150.0f,
                        30.0f
                    )
                )

                latitudeUser = location.latitude
                longitudeUser = location.longitude
            }
        }
    }

}


private fun calculateAndDisplayRoute(
    mapView: MapView,
    startPoint: Point,
    endPoint: Point
) {
    Log.d("tag", "fdfbdfjdnfjzjjjj")
    val directions = DirectionsFactory.getInstance()
    val session = directions.createDrivingRouter()

    val vehicleOptions = VehicleOptions()
    val drivingOptions = DrivingOptions()

    val requestPoints = listOf(
        RequestPoint(startPoint, RequestPointType.WAYPOINT, null),
        RequestPoint(endPoint, RequestPointType.WAYPOINT, null)
    )

    val drivingRouteListener = object : DrivingSession.DrivingRouteListener {

        override fun onDrivingRoutes(routes: MutableList<DrivingRoute>) {
            if (routes.isNotEmpty()) {
                val route = routes[0]
                val mapObjects = mapView.map.mapObjects
                mapObjects.clear()
                val routeLine = mapObjects.addPolyline(route.geometry)
            } else {
                // Handle the error
                Log.e("tag", "Error calculating route:")
            }
        }

        override fun onDrivingRoutesError(p0: Error) {
            Log.d("tag", "error: $p0")
        }
    }
    session.requestRoutes(
        requestPoints,
        drivingOptions,
        vehicleOptions,
        drivingRouteListener
    )
}


private var userLocationLayerAdded = false

private fun userLocationPoint(context: Context, mapView: MapView) {
    if (!userLocationLayerAdded) {
        val mapKit = MapKitFactory.getInstance()

        val userLocationOnMap = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocationOnMap.isVisible = true
        userLocationOnMap.setObjectListener(object : UserLocationObjectListener {
            override fun onObjectAdded(userLocationView: UserLocationView) {
                userLocationView.arrow.setIcon(
                    ImageProvider.fromResource(
                        context, R.drawable.currentlocation
                    ), IconStyle().setScale(0.1f)
                )
            }

            override fun onObjectRemoved(userLocationView: UserLocationView) = Unit
            override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) = Unit
        })

        userLocationLayerAdded = true
    }
}


private fun addOfficeMarkers(
    mapView: MapView,
    offices: List<OfficesItem>,
    context: Context,
    latitudeUser: Double,
    longitudeUser: Double,
) {
    for (office in offices) {
        val officePoint = Point(office.latitude, office.longitude)
        val officeMarker = mapView.map.mapObjects.addPlacemark(officePoint)

        officeMarker.setIcon(
            ImageProvider.fromResource(
                context, R.drawable.vtbicon
            ), IconStyle().setScale(0.1f)
        )
        officeMarker.addTapListener { _, _ ->

            calculateAndDisplayRoute(
                mapView,
                Point(latitudeUser, longitudeUser),
                officeMarker.geometry,
            )
            true
        }
    }
}


private fun buildRouteToOffice(
    mapView: MapView,
    startLatitude: Double,
    startLongitude: Double,
    endLatitude: Double,
    endLongitude: Double
) {
    Log.d("tag", "start $startLatitude")
    Log.d("tag", "start $startLongitude")
    Log.d("tag", "end $endLatitude")
    Log.d("tag", "end $endLongitude")
}

private fun buildRoute(
    startLocation: Point,
    endLocation: Point,
    mapView: MapView,
    showBottomSheet: MutableState<Boolean>
) {

    val drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
    val drivingOptions = DrivingOptions().apply {
        routesCount = 1
    }
    val vehicleOptions = VehicleOptions()
    val points = buildList {
        add(RequestPoint(startLocation, RequestPointType.WAYPOINT, null))
        add(RequestPoint(endLocation, RequestPointType.WAYPOINT, null))
    }
    val drivingRouteListener = object : DrivingSession.DrivingRouteListener {
        override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
            Log.d("tag", "fdhjdfjdzzzzzz")
            for (route in drivingRoutes) {
                mapView.map.mapObjects.addPolyline(route.geometry)
            }
        }

        override fun onDrivingRoutesError(p0: Error) {
            Log.d("tag", "error: $p0")
        }
    }

    drivingRouter.requestRoutes(
        points,
        drivingOptions,
        vehicleOptions,
        drivingRouteListener
    )
    showBottomSheet.value = true
}
