package com.example.bankfinderapp.feature_atm.domain.repository

import com.example.bankfinderapp.feature_atm.data.model.ATM
import com.example.bankfinderapp.feature_atm.data.model.FilterRequest

interface ATMRepository {
    suspend fun getATMs(lat: Double, long: Double, radius: Double): ATM

    suspend fun getFilterATMs(filterRequest: FilterRequest): ATM
}