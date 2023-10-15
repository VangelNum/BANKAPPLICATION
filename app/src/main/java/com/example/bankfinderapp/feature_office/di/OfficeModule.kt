package com.example.bankfinderapp.feature_office.di

import com.example.bankfinderapp.feature_office.data.api.ApiService
import com.example.bankfinderapp.feature_office.data.repository.OfficeRepositoryImpl
import com.example.bankfinderapp.feature_office.domain.repository.OfficesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class OfficeModule {
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOfficeRepository(apiService: ApiService): OfficesRepository {
        return OfficeRepositoryImpl(apiService)
    }
}