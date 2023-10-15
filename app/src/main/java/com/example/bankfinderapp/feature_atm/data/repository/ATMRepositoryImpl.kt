package com.example.bankfinderapp.feature_atm.data.repository

import com.example.bankfinderapp.feature_atm.data.api.ApiServiceATM
import com.example.bankfinderapp.feature_atm.data.model.ATM
import com.example.bankfinderapp.feature_atm.domain.repository.ATMRepository
import javax.inject.Inject

class ATMRepositoryImpl @Inject constructor(
    private val apiService: ApiServiceATM
): ATMRepository {
    override suspend fun getATMs(lat: Double, long: Double, radius: Double): ATM {
        return apiService.getATMs(lat, long, radius)
    }
}