package com.iberdrola.practicas2026.DanielFJ.data

data class InvoiceDetail(
    val id: String, val type: String, val amount: Double,
    val startDate: String, val endDate: String, val status: String
)