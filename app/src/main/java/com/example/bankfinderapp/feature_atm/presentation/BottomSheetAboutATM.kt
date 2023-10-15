package com.example.bankfinderapp.feature_atm.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bankfinderapp.feature_atm.data.model.ATMModelItem
import com.example.bankfinderapp.feature_map.presentation.displayRoute
import com.example.unisoldevtestwork.R
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.mapview.MapView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetAboutATM(
    selectedATM: MutableState<ATMModelItem?>,
    showBottomSheet: MutableState<Boolean>,
    mapView: MapView,
    latitudeUser: MutableDoubleState,
    longitudeUser: MutableDoubleState
) {
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
                    selectedATM.value?.let { model ->
                        Text("Адрес: ${model.address}")
                        Row {

                            Text(
                                text = "Круглосуточно:",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = if (selectedATM.value!!.allDay) "Да" else "Нет")
                        }

                        Text(
                            text = "Доступные услуги:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Wheelchair ${selectedATM.value!!.features.wheelchair}")
                        Text("Blind ${selectedATM.value!!.features.wheelchair}")
                        Text("NFC for Bank Cards ${selectedATM.value!!.features.wheelchair}")
                        Text("QR Read ${selectedATM.value!!.features.wheelchair}")
                        Text("Supports USD ${selectedATM.value!!.features.wheelchair}")
                        Text("Supports Charge in Rubles ${selectedATM.value!!.features.wheelchair}")
                        Text("Supports EUR ${selectedATM.value!!.features.wheelchair}")
                        Text("Supports Charge in Rubles ${selectedATM.value!!.features.wheelchair}")
                        Text("Supports Rubles ${selectedATM.value!!.features.wheelchair}")

                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    val fontBigFamily = FontFamily(
                        Font(R.font.ubuntumedium)
                    )
                    Button(
                        onClick = {
                            selectedATM.value?.let { office ->
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

