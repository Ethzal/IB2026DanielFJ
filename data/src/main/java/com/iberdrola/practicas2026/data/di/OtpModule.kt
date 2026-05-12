package com.iberdrola.practicas2026.data.di

import com.iberdrola.practicas2026.data.repository.OtpRepositoryImpl
import com.iberdrola.practicas2026.domain.repository.OtpRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OtpModule {
    @Binds
    @Singleton
    abstract fun bindOtpRepository(impl: OtpRepositoryImpl): OtpRepository
}