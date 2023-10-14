package com.example.unisoldevtestwork.feature_office.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import com.example.unisoldevtestwork.feature_office.data.model.OfficesItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetAbout(
    offices: State<List<OfficesItem>>,
    showBottomSheet: MutableState<Boolean>
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet.value = false
            },
            sheetState = sheetState
        ) {
            LazyColumn {
                items(offices.value) { officeItem ->
                    Text(officeItem.address)
//                    Text(officeItem.officeType)
//                    Text(officeItem.hasRamp)
//                    Text(officeItem.metroStation)
//                    Text(officeItem.status)
                }
            }
        }
    }
}