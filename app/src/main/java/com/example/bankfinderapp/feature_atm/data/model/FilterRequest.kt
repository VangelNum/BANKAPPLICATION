package com.example.bankfinderapp.feature_atm.data.model

data class FilterRequest(
    val lat: Double,
    val long: Double,
    val rad: Double,
    val types: List<String>
)