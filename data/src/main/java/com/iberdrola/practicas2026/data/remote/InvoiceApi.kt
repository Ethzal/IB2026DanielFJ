package com.iberdrola.practicas2026.data.remote

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import retrofit2.http.GET

interface InvoiceApi {

    @Mock
    @MockResponse(body = "invoices.json")
    @GET("getInvoices")
    suspend fun getInvoices(): InvoiceResponseDto
}