package com.example.bankfinderapp.feature_office.domain.repository

import com.example.bankfinderapp.feature_office.data.model.Offices

interface OfficesRepository {
    suspend fun getOffices(lat: Double, long: Double, rad: Double): Offices
}