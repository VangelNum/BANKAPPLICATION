package com.example.unisoldevtestwork.feature_map.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unisoldevtestwork.R
import com.example.unisoldevtestwork.feature_map.presentation.utils.ServiceItems

@Composable
fun BottomSheetContainer() {
    val imageTextServiceList = listOf(
        ServiceItems(R.drawable.personwheelchair, R.string.WHEELCHAIR),
        ServiceItems(R.drawable.eyeglasses, R.string.BLIND),
        ServiceItems(R.drawable.creditcardfill, R.string.NFC_FOR_BANK_CARDS),
        ServiceItems(R.drawable.qrcode, R.string.QR_READ),
        ServiceItems(R.drawable.currencydollar, R.string.SUPPORTS_USD),
        ServiceItems(R.drawable.currencydollar, R.string.SUPPORTS_CHARGE_RUB),
        ServiceItems(R.drawable.currencyeuro, R.string.SUPPORTS_EUR),
        ServiceItems(R.drawable.currencydollar, R.string.SUPPORTS_RUB)
    )
    IconGrid(imageTextServiceList)
}


@Composable
fun ServiceItem(serviceItem: ServiceItems) {
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = serviceItem.drawRes),
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = serviceItem.text),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            style = TextStyle()
        )
    }
}

@Composable
fun IconGrid(iconTextList: List<ServiceItems>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .fillMaxHeight(0.5f)
            .padding(16.dp)
    ) {
        items(iconTextList) { iconTextItem ->
            ServiceItem(serviceItem = iconTextItem)
        }
    }
}