package com.iberdrola.practicas2026.data.remote

import com.iberdrola.practicas2026.data.repository.InvoiceRepositoryImpl
import com.iberdrola.practicas2026.domain.repository.InvoiceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindInvoiceRepository(
        impl: InvoiceRepositoryImpl
    ): InvoiceRepository
}