package com.example.bankfinderapp.feature_atm.domain.repository

import com.example.bankfinderapp.feature_atm.data.model.ATM

interface ATMRepository {
    suspend fun getATMs(lat: Double, long: Double, radius: Double): ATM
}