package com.example.bankfinderapp.feature_atm.data.api

import com.example.bankfinderapp.feature_atm.data.model.ATM
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServiceATM {
    @GET("api/v1/atms")
    suspend fun getATMs(
        @Query("lat") lat: Double,
        @Query("long") long: Double,
        @Query("rad") radius: Double
    ): ATM

    @POST("api/v1/atms/filter")
    suspend fun filterAtms(
        @Query("lat") lat: Double,
        @Query("long") long: Double,
        @Query("rad") rad: Double,
        @Body body: List<String>
    ): ATM
}