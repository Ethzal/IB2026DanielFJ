package com.iberdrola.practicas2026.data.di

import android.content.Context
import co.infinum.retromock.Retromock
import com.iberdrola.practicas2026.data.remote.InvoiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val MOCKOON_URL = "http://localhost:3000/" // adb reverse tcp:3000 tcp:3000

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(MOCKOON_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideRetromock(retrofit: Retrofit, @ApplicationContext context: Context): Retromock =
        Retromock.Builder()
            .retrofit(retrofit)
            .defaultBodyFactory(context.assets::open)
            .build()

    @Provides
    @Singleton
    @RemoteApi
    fun provideRemoteInvoiceApi(retrofit: Retrofit): InvoiceApi =
        retrofit.create(InvoiceApi::class.java)

    @Provides
    @Singleton
    @LocalApi
    fun provideLocalInvoiceApi(retromock: Retromock): InvoiceApi =
        retromock.create(InvoiceApi::class.java)
}