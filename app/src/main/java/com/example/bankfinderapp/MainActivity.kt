package com.example.bankfinderapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bankfinderapp.feature_atm.data.model.ATMModelItem
import com.example.bankfinderapp.feature_atm.presentation.ATMViewModel
import com.example.bankfinderapp.feature_atm.presentation.BottomSheetAboutATM
import com.example.bankfinderapp.feature_map.presentation.BottomSheetServicesForATM
import com.example.bankfinderapp.feature_map.presentation.ErrorPermissionLocation
import com.example.bankfinderapp.feature_map.presentation.MapViewContainer
import com.example.bankfinderapp.feature_map.presentation.addAtmsMarkers
import com.example.bankfinderapp.feature_map.presentation.addOfficeMarkers
import com.example.bankfinderapp.feature_map.presentation.clearRoute
import com.example.bankfinderapp.feature_office.data.model.OfficesItem
import com.example.bankfinderapp.feature_office.presentation.BottomSheetAboutOffices
import com.example.bankfinderapp.feature_office.presentation.OfficesViewModel
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
        //MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
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
                    val offices = officesViewModel.offices.collectAsStateWithLifecycle()
                    val atmViewModel = hiltViewModel<ATMViewModel>()
                    val atms = atmViewModel.atms.collectAsStateWithLifecycle()
                    val locationTrackingGranted = checkPermission()

                    if (locationTrackingGranted) {
                        val showBottomSheetForOffices = remember { mutableStateOf(false) }

                        val showBottomSheetForATM = remember { mutableStateOf(false) }

                        val selectedOffice = remember {
                            mutableStateOf<OfficesItem?>(null)
                        }

                        val selectedATM = remember {
                            mutableStateOf<ATMModelItem?>(null)
                        }

                        val latitudeUser = remember {
                            mutableDoubleStateOf(0.0)
                        }
                        val longitudeUser = remember {
                            mutableDoubleStateOf(0.0)
                        }
                        val context = LocalContext.current

                        Scaffold() { contentPadding ->
                            MapViewContainer(
                                mapView = mapView,
                                fusedLocationClient = fusedLocationClient,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(contentPadding),
                                onSearchOffices = { lat, long, rad ->
                                    officesViewModel.fetchOffices(lat, long, rad)
                                },
                                onSearchATM = { lat, long, rad ->
                                    atmViewModel.fetchAtms(lat, long, rad)
                                },
                                latitudeUser = latitudeUser,
                                longitudeUser = longitudeUser
                            )
                            var isOfficeChipSelected by remember { mutableStateOf(false) }
                            var isATMChipSelected by remember { mutableStateOf(false) }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 8.dp, start = 16.dp)
                            ) {
                                Row {
                                    SuggestionChip(
                                        onClick = {
                                            mapView.map.mapObjects.clear()
                                            addOfficeMarkers(
                                                mapView,
                                                offices.value,
                                                context,
                                                showBottomSheetForOffices,
                                                selectedOffice
                                            )
                                            isOfficeChipSelected = true
                                            isATMChipSelected = false
                                        },
                                        label = {
                                            Text(
                                                "Офисы",
                                                color = if (isOfficeChipSelected) Color.Blue else Color.Black
                                            )
                                        }
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    SuggestionChip(
                                        onClick = {
                                            mapView.map.mapObjects.clear()
                                            addAtmsMarkers(
                                                mapView,
                                                atms.value,
                                                context,
                                                showBottomSheetForATM,
                                                selectedATM
                                            )
                                            isOfficeChipSelected = false
                                            isATMChipSelected = true
                                        },
                                        label = {
                                            Text(
                                                "Банкоматы",
                                                color = if (isATMChipSelected) Color.Blue else Color.Black
                                            )
                                        }
                                    )
                                }
                            }

                            if (!showBottomSheetForOffices.value && !showBottomSheetForATM.value && isATMChipSelected) {
                                BottomSheetServicesForATM(
                                    atmViewModel,
                                    latitudeUser,
                                    longitudeUser,
                                    10000.0,
                                    onAddedFilter = {
                                        mapView.map.mapObjects.clear()
                                        addAtmsMarkers(
                                            mapView,
                                            atms.value,
                                            context,
                                            showBottomSheetForATM,
                                            selectedATM
                                        )
                                    }
                                )
                            } else {
                                if (selectedOffice.value != null && showBottomSheetForOffices.value && isOfficeChipSelected) {
                                    Column {
                                        Box(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .padding(top = 40.dp)
                                                .size(50.dp)
                                                .background(
                                                    MaterialTheme.colorScheme.primary,
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            IconButton(modifier = Modifier.size(35.dp),
                                                onClick = {
                                                    clearRoute(mapView)
                                                    showBottomSheetForOffices.value = false
                                                }
                                            ) {
                                                Icon(
                                                    modifier = Modifier.size(35.dp),
                                                    imageVector = Icons.Filled.KeyboardArrowLeft,
                                                    tint = Color.White,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                        BottomSheetAboutOffices(
                                            selectedOffice = selectedOffice,
                                            showBottomSheet = showBottomSheetForOffices,
                                            mapView = mapView,
                                            latitudeUser = latitudeUser,
                                            longitudeUser = longitudeUser
                                        )
                                    }
                                }
                                if (selectedATM.value != null && showBottomSheetForATM.value && isATMChipSelected) {
                                    Column {
                                        Box(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .padding(top = 40.dp)
                                                .size(50.dp)
                                                .background(
                                                    MaterialTheme.colorScheme.primary,
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            IconButton(modifier = Modifier.size(35.dp),
                                                onClick = {
                                                    showBottomSheetForATM.value = false
                                                    clearRoute(mapView)
                                                }
                                            ) {
                                                Icon(
                                                    modifier = Modifier.size(35.dp),
                                                    imageVector = Icons.Filled.KeyboardArrowLeft,
                                                    tint = Color.White,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                        BottomSheetAboutATM(
                                            selectedATM = selectedATM,
                                            showBottomSheet = showBottomSheetForATM,
                                            mapView = mapView,
                                            latitudeUser = latitudeUser,
                                            longitudeUser = longitudeUser
                                        )
                                    }
                                }
                            }
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
