package com.example.bankfinderapp.feature_atm.di

import com.example.bankfinderapp.feature_atm.data.api.ApiServiceATM
import com.example.bankfinderapp.feature_atm.data.repository.ATMRepositoryImpl
import com.example.bankfinderapp.feature_atm.domain.repository.ATMRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ATMModule {
    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiServiceATM {
        return retrofit.create(ApiServiceATM::class.java)
    }

    @Provides
    @Singleton
    fun provideATMRepository(apiService: ApiServiceATM): ATMRepository {
        return ATMRepositoryImpl(apiService)
    }
}