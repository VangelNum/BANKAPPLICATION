package com.example.bankfinderapp.feature_office.data.model

data class OfficesItem(
    val address: String,
    val distance: Int?,
    val hasRamp: String?,
    val kep: Boolean?,
    val latitude: Double,
    val longitude: Double,
    val metroStation: String?,
    val myBranch: Boolean?,
    val officeType: String?,
    val openHours: List<OpenHour>?,
    val openHoursIndividual: List<OpenHoursIndividual>?,
    val rko: String?,
    val routeDuration: Int?,
    val salePointFormat: String?,
    val salePointName: String?,
    val status: String?,
    val suoAvailability: String?
)