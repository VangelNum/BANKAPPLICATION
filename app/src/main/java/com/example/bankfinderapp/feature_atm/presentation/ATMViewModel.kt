package com.example.bankfinderapp.feature_atm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankfinderapp.feature_atm.data.model.ATM
import com.example.bankfinderapp.feature_atm.data.model.FilterRequest
import com.example.bankfinderapp.feature_atm.domain.repository.ATMRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ATMViewModel @Inject constructor(
    private val repository: ATMRepository
) : ViewModel() {
    private val _atm = MutableStateFlow<ATM>(ATM())
    val atms = _atm.asStateFlow()

    fun fetchAtms(lat: Double, long: Double, rad: Double) {
        viewModelScope.launch {
            val result = repository.getATMs(lat, long, rad)
            _atm.value = result
        }
    }

    fun filterATMs(filterRequest: FilterRequest) {
        viewModelScope.launch {
            val result = repository.getFilterATMs(filterRequest)
            _atm.value = result
        }
    }
}