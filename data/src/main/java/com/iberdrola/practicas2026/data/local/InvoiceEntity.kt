package com.iberdrola.practicas2026.data.local

@androidx.room.Entity(tableName = "invoices")
data class InvoiceEntity(
    @androidx.room.PrimaryKey val id: String,
    val date: String,
    val type: String,
    val amount: Double,
    val status: String
)
