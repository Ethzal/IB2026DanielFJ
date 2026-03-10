package com.iberdrola.practicas2026.domain.model

data class InvoiceDetail(
    val id: String, val type: String, val amount: Double,
    val startDate: String, val endDate: String, val status: String
)