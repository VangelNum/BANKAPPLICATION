package com.example.bankfinderapp.feature_map.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bankfinderapp.feature_atm.data.model.FilterRequest
import com.example.bankfinderapp.feature_atm.presentation.ATMViewModel
import com.example.bankfinderapp.feature_map.presentation.utils.ServiceItems
import com.example.unisoldevtestwork.R

@Composable
fun BottomSheetServicesForATM(
    atmViewModel: ATMViewModel,
    latitudeUser: MutableDoubleState,
    longitudeUser: MutableDoubleState,
    rad: Double,
    onAddedFilter:() -> Unit
) {
    val imageTextServiceList = listOf(
        ServiceItems(R.drawable.personwheelchair, R.string.WHEELCHAIR, "WHEELCHAIR"),
        ServiceItems(R.drawable.eyeglasses, R.string.BLIND, "BLIND"),
        ServiceItems(R.drawable.creditcardfill, R.string.NFC_FOR_BANK_CARDS, "NFC_FOR_BANK_CARDS"),
        ServiceItems(R.drawable.qrcode, R.string.QR_READ, "QR_READ"),
        ServiceItems(R.drawable.currencydollar, R.string.SUPPORTS_USD, "SUPPORTS_USD"),
        ServiceItems(R.drawable.rubicon, R.string.SUPPORTS_CHARGE_RUB, "SUPPORTS_CHARGE_RUB"),
        ServiceItems(R.drawable.currencyeuro, R.string.SUPPORTS_EUR, "SUPPORTS_EUR"),
        ServiceItems(R.drawable.rubicon, R.string.SUPPORTS_RUB, "SUPPORTS_RUB")
    )
    IconGrid(
        imageTextServiceList, atmViewModel,
        latitudeUser,
        longitudeUser,
        rad,
        onAddedFilter
    )
}
@Composable
fun ServiceItem(
    serviceItem: ServiceItems,
    atmViewModel: ATMViewModel,
    latitudeUser: MutableDoubleState,
    longitudeUser: MutableDoubleState,
    rad: Double,
    onAddedFilter: () -> Unit,
    isSelected: Boolean,
    selectedServices: List<ServiceItems>,
    onServiceSelected: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(
                    if (isSelected) Color.Blue
                    else MaterialTheme.colorScheme.secondary,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = serviceItem.drawRes),
                contentDescription = null,
                modifier = Modifier
                    .size(if (isSelected) 45.dp else 35.dp)
                    .clickable {
                        val filterRequest = FilterRequest(
                            latitudeUser.value,
                            longitudeUser.value,
                            rad,
                            selectedServices.map { it.name }
                        )
                        atmViewModel.filterATMs(filterRequest)
                        onAddedFilter()
                        // Вызовите функцию onServiceSelected для обновления выбранных сервисов
                        onServiceSelected()
                    }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = serviceItem.text),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = TextStyle()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconGrid(
    iconTextList: List<ServiceItems>,
    atmViewModel: ATMViewModel,
    latitudeUser: MutableDoubleState,
    longitudeUser: MutableDoubleState,
    rad: Double,
    onAddedFilter: () -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = SheetValue.Expanded,
            skipPartiallyExpanded = false
        )
    )

    // Список выбранных сервисов
    var selectedServices by remember { mutableStateOf(emptyList<ServiceItems>()) }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .padding(16.dp)
            ) {
                items(iconTextList) { iconTextItem ->
                    val isSelected = iconTextItem in selectedServices

                    // Вызывайте функцию `onServiceSelected` при выборе или отмене выбора сервиса
                    ServiceItem(
                        serviceItem = iconTextItem,
                        atmViewModel,
                        latitudeUser,
                        longitudeUser,
                        rad,
                        onAddedFilter,
                        isSelected,
                        selectedServices,
                    ) {
                        if (isSelected) {
                            // Удалите сервис из списка выбранных, если он был выбран
                            selectedServices = selectedServices - iconTextItem
                        } else {
                            // Добавьте сервис в список выбранных, если он был отменен
                            selectedServices = selectedServices + iconTextItem
                        }
                    }
                }
            }
        }
    ) {}
}
