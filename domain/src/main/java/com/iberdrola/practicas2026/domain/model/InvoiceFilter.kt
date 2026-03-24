package com.iberdrola.practicas2026.domain.model

data class InvoiceFilter(
    val dateFrom: String? = null,
    val dateTo: String? = null,
    val amountRange: ClosedFloatingPointRange<Float>? = null,
    val statuses: Set<String> = emptySet()
)