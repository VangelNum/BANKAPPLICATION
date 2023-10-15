package com.example.bankfinderapp.feature_atm.data.model

data class ATMModelItem(
    val address: String,
    val allDay: Boolean,
    val features: Features,
    val latitude: Double,
    val longitude: Double
)