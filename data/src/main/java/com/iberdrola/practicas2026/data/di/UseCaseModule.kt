package com.iberdrola.practicas2026.data.di

import com.iberdrola.practicas2026.domain.repository.InvoiceRepository
import com.iberdrola.practicas2026.domain.usecase.GetInvoicesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetInvoicesUseCase(
        repository: InvoiceRepository
    ): GetInvoicesUseCase {
        return GetInvoicesUseCase(repository)
    }
}