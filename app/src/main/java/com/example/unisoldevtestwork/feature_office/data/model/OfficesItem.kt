package com.example.unisoldevtestwork.feature_office.data.model

data class OfficesItem(
    val address: String,
    val addressString: String,
    val hasRamp: String, //для инвалидов
    val kep: Boolean, //
    val latitude: Double,
    val longitude: Double,
    val metroStation: String,
    val myBranch: Boolean,
    val officeType: String,
    val rko: String,
    val salePointFormat: String,
    val salePointName: String,
    val status: String,
    val suoAvailability: String
)