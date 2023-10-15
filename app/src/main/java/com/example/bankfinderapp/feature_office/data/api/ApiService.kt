package com.example.bankfinderapp.feature_office.data.api

import com.example.bankfinderapp.feature_office.data.model.Offices
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/v1/offices")
    suspend fun getOffices(
        @Query("lat") lat: Double,
        @Query("long") long: Double,
        @Query("rad") rad: Double
    ): Offices
}