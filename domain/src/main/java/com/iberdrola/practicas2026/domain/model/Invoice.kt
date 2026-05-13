package com.iberdrola.practicas2026.domain.model

data class Invoice(
    val id: String,
    val date: String,
    val type: String,
    val amount: Double,
    val status: InvoiceStatus,
    val startDate: String = "",
    val endDate: String = ""
)