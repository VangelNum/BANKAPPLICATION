package com.example.bankfinderapp.feature_map.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.bankfinderapp.feature_atm.data.model.ATM
import com.example.bankfinderapp.feature_atm.data.model.ATMModelItem
import com.example.bankfinderapp.feature_office.data.model.OfficesItem
import com.example.unisoldevtestwork.R
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
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider


@SuppressLint("VisibleForTests", "MissingPermission")
@Composable
fun MapViewContainer(
    mapView: MapView,
    fusedLocationClient: FusedLocationProviderClient,
    modifier: Modifier = Modifier,
    onSearchOffices: (lat: Double, long: Double, rad: Double) -> Unit,
    onSearchATM: (lat: Double, long: Double, rad: Double) -> Unit,
    latitudeUser: MutableDoubleState,
    longitudeUser: MutableDoubleState
) {
    val context = LocalContext.current
    var firstLaunch by remember {
        mutableStateOf(true)
    }
    userLocationPoint(context, mapView)

    AndroidView(
        modifier = modifier,
        factory = { mapView }
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                //onSearchOffices(location.latitude, location.longitude, 10000.0)
                if (firstLaunch) {
                    onSearchOffices(55.721281, 37.555647, 12.0)
                    onSearchATM(55.721281, 37.555647, 12.0)
                    mapView.map.move(
                        CameraPosition(
                            //Point(location.latitude, location.longitude),
                            Point(55.721281, 37.555647),
                            17.0f,
                            150.0f,
                            30.0f
                        )
                    )
                    latitudeUser.value = location.latitude
                    longitudeUser.value = location.longitude
                    firstLaunch = false
                }
            }
        }
    }
}

fun addAtmsMarkers(
    mapView: MapView,
    atms: ATM,
    context: Context,
    showATMBottomSheet: MutableState<Boolean>,
    selectedATM: MutableState<ATMModelItem?>
) {
    for (atm in atms) {
        val atmPoint = Point(atm.latitude, atm.longitude)
        val atmMarker = mapView.map.mapObjects.addPlacemark(atmPoint)

        atmMarker.setIcon(
            ImageProvider.fromResource(
                context, R.drawable.vtbicon
            ), IconStyle().setScale(0.1f)
        )

        atmMarker.addTapListener { mapObject, _ ->
            showATMBottomSheet.value = true
            selectedATM.value = atm
            true
        }
    }
}

fun addOfficeMarkers(
    mapView: MapView,
    offices: List<OfficesItem>,
    context: Context,
    showOfficeBottomSheet: MutableState<Boolean>,
    selectedOffice: MutableState<OfficesItem?>
) {
    for (office in offices) {
        val officePoint = Point(office.latitude, office.longitude)
        val officeMarker = mapView.map.mapObjects.addPlacemark(officePoint)

        officeMarker.setIcon(
            ImageProvider.fromResource(
                context, R.drawable.vtbicon
            ), IconStyle().setScale(0.1f)
        )
        officeMarker.addTapListener { mapObject, _ ->
            showOfficeBottomSheet.value = true
            selectedOffice.value = office
            true
        }
    }
}



private val routePolylines: MutableList<PolylineMapObject> = mutableListOf()

fun displayRoute(
    mapView: MapView,
    startPoint: Point,
    endPoint: Point,
) {
    for (polyline in routePolylines) {
        mapView.map.mapObjects.remove(polyline)
    }
    routePolylines.clear()

    val session = DirectionsFactory.getInstance().createDrivingRouter()

    val drivingOptions = DrivingOptions().apply {
        routesCount = 1
    }
    val vehicleOptions = VehicleOptions()

    val requestPoints = listOf(
        RequestPoint(startPoint, RequestPointType.WAYPOINT, null),
        RequestPoint(endPoint, RequestPointType.WAYPOINT, null)
    )

    val drivingRouteListener = object : DrivingSession.DrivingRouteListener {

        override fun onDrivingRoutes(routes: MutableList<DrivingRoute>) {
            for (route in routes) {
                val polyline = mapView.map.mapObjects.addPolyline(route.geometry)
                routePolylines.add(polyline)
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



