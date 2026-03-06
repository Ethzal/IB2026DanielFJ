package com.iberdrola.practicas2026.DanielFJ.data

data class InvoiceResponse(
    val lastInvoice: InvoiceDetail,
    val history: List<InvoiceItem>
)