package com.iberdrola.practicas2026.core.di

import com.iberdrola.practicas2026.data.remote.InvoiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides // CORREGIDO: Eliminado JBRApi
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://tu-url-de-mockable.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): InvoiceApi = retrofit.create(InvoiceApi::class.java)
}