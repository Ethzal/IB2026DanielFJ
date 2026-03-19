package com.iberdrola.practicas2026.data.di

import android.content.Context
import android.os.Build
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

    private const val EMULATOR_URL = "http://10.0.2.2:3000/"
    private const val DEVICE_URL = "http://localhost:3000/" // adb reverse tcp:3000 tcp:3000

    private fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.lowercase().contains("vbox")
                || Build.FINGERPRINT.lowercase().contains("test-keys")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || "google_sdk" == Build.PRODUCT)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val baseUrl = if (isEmulator()) EMULATOR_URL else DEVICE_URL
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

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