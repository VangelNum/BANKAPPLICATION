package com.example.unisoldevtestwork

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.unisoldevtestwork.feature_map.presentation.ErrorPermissionLocation
import com.example.unisoldevtestwork.feature_map.presentation.MapViewContainer
import com.example.unisoldevtestwork.feature_office.presentation.ModalBottomSheetAbout
import com.example.unisoldevtestwork.feature_office.presentation.OfficesViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vangelnum.bankfinderapp.ui.theme.BankFinderAppTheme
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // MapKitFactory.setApiKey( BuildConfig.MAPKIT_API_KEY)
        MapKitFactory.setApiKey("74a45254-7bef-4167-916a-39bbef18987d")

        MapKitFactory.initialize(this)
        mapView = MapView(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            BankFinderAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val officesViewModel = hiltViewModel<OfficesViewModel>()
                    val offices = officesViewModel.offices.collectAsState()
                    val locationTrackingGranted = checkPermission()
                    if (locationTrackingGranted) {
                        var showBottomSheet = remember { mutableStateOf(false) }
                        Scaffold(

                        ) { contentPadding ->
                            MapViewContainer(
                                offices = offices,
                                mapView = mapView,
                                fusedLocationClient = fusedLocationClient,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(contentPadding),
                                onSearchOffices = { lat, long, rad ->
                                    officesViewModel.fetchOffices(lat, long, rad)
                                },
                                showBottomSheet = showBottomSheet
                            )
                            ModalBottomSheetAbout(offices, showBottomSheet)
                        }
                    } else {
                        ErrorPermissionLocation()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun checkPermission(): Boolean {
    val lifecycleOwner = LocalLifecycleOwner.current
    val permissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    permissionState.launchPermissionRequest()
                }

                else -> {

                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    return permissionState.status.isGranted
}
