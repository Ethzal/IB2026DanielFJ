package com.iberdrola.practicas2026.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.iberdrola.practicas2026.data.repository.SettingsRepositoryImpl
import com.iberdrola.practicas2026.domain.repository.SettingsRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
}