package com.example.bankfinderapp.feature_office.presentation

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bankfinderapp.feature_map.presentation.displayRoute
import com.example.bankfinderapp.feature_office.data.model.OfficesItem
import com.example.unisoldevtestwork.R
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.mapview.MapView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetAboutOffices(
    selectedOffice: MutableState<OfficesItem?>,
    showBottomSheet: MutableState<Boolean>,
    mapView: MapView,
    latitudeUser: MutableDoubleState,
    longitudeUser: MutableDoubleState
) {
    val onBack = { showBottomSheet.value = false }
    BackPressHandler(onBackPressed = onBack)

    val scope = rememberCoroutineScope()

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = SheetValue.Expanded,
            skipPartiallyExpanded = false
        )
    )

    AnimatedVisibility(
        showBottomSheet.value,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                Column(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp,
                        top = 8.dp
                    ),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val fontFamily = FontFamily(
                        Font(R.font.ubuntylight)
                    )
                    Text(
                        text = "Отделение",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                    )
                    selectedOffice.value?.let {
                        Text("Адрес: ${it.address}")
                        it.status?.let { status ->
                            Text(
                                "Статус: $status",
                                fontFamily = fontFamily
                            )
                        }
                        it.hasRamp?.let { hasRamp ->
                            Text(
                                "Пандус: $hasRamp",
                                fontFamily = fontFamily
                            )
                        }
                        it.metroStation?.let { metroStation ->
                            Text(
                                "Станция метро: $metroStation",
                                fontFamily = fontFamily
                            )
                        }
                        it.officeType?.let { officeType ->
                            Text(
                                "Тип офиса: $officeType",
                                fontFamily = fontFamily
                            )
                        }
                        it.rko?.let { rko -> Text("РКО: $rko", fontFamily = fontFamily) }
                        it.salePointFormat?.let { salePointFormat ->
                            Text(
                                "Формат ТП: $salePointFormat",
                                fontFamily = fontFamily
                            )
                        }
                        it.salePointName?.let { salePointName ->
                            Text(
                                "Точка продаж: $salePointName",
                                fontFamily = fontFamily
                            )
                        }
                        it.suoAvailability?.let { suoAvailability ->
                            Text(
                                "Наличие СУО: $suoAvailability",
                                fontFamily = fontFamily
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    val fontBigFamily = FontFamily(
                        Font(R.font.ubuntumedium)
                    )
                    Button(
                        onClick = {
                            selectedOffice.value?.let { office ->
                                displayRoute(
                                    mapView = mapView,
                                    startPoint = Point(latitudeUser.value, longitudeUser.value),
                                    endPoint = Point(office.latitude, office.longitude),
                                )
                            }
                            scope.launch {
                                if (showBottomSheet.value) {
                                    scaffoldState.bottomSheetState.expand()
                                }
                                scaffoldState.bottomSheetState.hide()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text("Проложить маршрут", fontSize = 20.sp, fontFamily = fontBigFamily)
                    }
                }
            }
        ) {

        }

    }
}


@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}
