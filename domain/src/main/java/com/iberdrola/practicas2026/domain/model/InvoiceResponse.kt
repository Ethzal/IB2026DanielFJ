package com.iberdrola.practicas2026.domain.model

data class InvoiceResponse(
    val lastInvoice: Invoice? = null,
    val history: List<Invoice> = emptyList(),
    val allInvoices: List<Invoice> = emptyList()
)