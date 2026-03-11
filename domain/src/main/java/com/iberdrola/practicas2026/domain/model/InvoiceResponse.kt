package com.iberdrola.practicas2026.domain.model

data class InvoiceResponse(
    val lastInvoice: InvoiceDetail,
    val history: List<InvoiceItem>
)