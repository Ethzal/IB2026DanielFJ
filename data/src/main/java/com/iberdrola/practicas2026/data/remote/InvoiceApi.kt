package com.iberdrola.practicas2026.data.remote

import com.iberdrola.practicas2026.domain.model.InvoiceResponse
import retrofit2.http.GET

interface InvoiceApi {
    @GET("invoices.json")
    suspend fun getRemoteInvoices(): InvoiceResponse
}