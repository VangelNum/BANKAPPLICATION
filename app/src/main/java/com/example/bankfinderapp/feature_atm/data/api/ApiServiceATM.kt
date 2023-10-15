package com.example.bankfinderapp.feature_atm.data.api

import com.example.bankfinderapp.feature_atm.data.model.ATM
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceATM {
    @GET("api/v1/atms")
    suspend fun getATMs(
        @Query("lat") lat: Double,
        @Query("long") long: Double,
        @Query("rad") radius: Double
    ): ATM
}