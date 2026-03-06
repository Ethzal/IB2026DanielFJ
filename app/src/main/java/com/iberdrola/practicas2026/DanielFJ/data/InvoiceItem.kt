package com.iberdrola.practicas2026.DanielFJ.data

data class InvoiceItem(
    val id: String, val date: String, val type: String,
    val amount: Double, val status: String
)