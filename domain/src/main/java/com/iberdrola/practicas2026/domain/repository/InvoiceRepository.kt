package com.iberdrola.practicas2026.domain.repository

import com.iberdrola.practicas2026.domain.model.InvoiceResponse
import kotlinx.coroutines.flow.Flow

interface InvoiceRepository {
    suspend fun getInvoices(isLocal: Boolean): Flow<InvoiceResponse>
}