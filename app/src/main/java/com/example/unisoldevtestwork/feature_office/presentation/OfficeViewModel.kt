package com.example.unisoldevtestwork.feature_office.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unisoldevtestwork.feature_office.data.model.Offices
import com.example.unisoldevtestwork.feature_office.domain.repository.OfficesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfficesViewModel @Inject constructor(
    private val repository: OfficesRepository
) : ViewModel() {

    private val _offices = MutableStateFlow<Offices>(Offices())
    val offices  = _offices.asStateFlow()

    fun fetchOffices(lat: Double, long: Double, rad: Double) {
        viewModelScope.launch {
            val result = repository.getOffices(lat, long, rad)
            _offices.value = result
        }
    }
}