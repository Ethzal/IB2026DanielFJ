package com.iberdrola.practicas2026.domain.model

enum class InvoiceType(val value: String) {
    LIGHT("Factura Luz"),
    GAS("Factura Gas"),
    UNKNOWN("")
}