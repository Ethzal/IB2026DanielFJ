package com.iberdrola.practicas2026.data.di

import com.iberdrola.practicas2026.data.repository.AnalyticsRepositoryImpl
import com.iberdrola.practicas2026.data.repository.RemoteConfigRepositoryImpl
import com.iberdrola.practicas2026.domain.repository.AnalyticsRepository
import com.iberdrola.practicas2026.domain.repository.RemoteConfigRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseModule { // adb shell setprop debug.firebase.analytics.app com.iberdrola.practicas2026.DanielFJ
    @Binds
    @Singleton
    abstract fun bindAnalyticsRepository(impl: AnalyticsRepositoryImpl): AnalyticsRepository

    @Binds
    @Singleton
    abstract fun bindRemoteConfigRepository(impl: RemoteConfigRepositoryImpl): RemoteConfigRepository
}