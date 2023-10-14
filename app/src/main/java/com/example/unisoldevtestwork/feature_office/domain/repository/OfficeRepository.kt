package com.example.unisoldevtestwork.feature_office.domain.repository

import com.example.unisoldevtestwork.feature_office.data.model.Offices

interface OfficesRepository {
    suspend fun getOffices(lat: Double, long: Double, rad: Double): Offices
}