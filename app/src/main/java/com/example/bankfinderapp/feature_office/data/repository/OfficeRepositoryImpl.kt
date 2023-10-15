package com.example.bankfinderapp.feature_office.data.repository

import com.example.bankfinderapp.feature_office.data.api.ApiService
import com.example.bankfinderapp.feature_office.data.model.Offices
import com.example.bankfinderapp.feature_office.domain.repository.OfficesRepository
import javax.inject.Inject

class OfficeRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : OfficesRepository {
    override suspend fun getOffices(lat: Double, long: Double, rad: Double): Offices {
        return apiService.getOffices(lat, long, rad)
    }
}