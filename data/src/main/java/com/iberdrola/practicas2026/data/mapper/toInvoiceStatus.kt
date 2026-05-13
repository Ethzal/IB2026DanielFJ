package com.iberdrola.practicas2026.data.mapper

import com.iberdrola.practicas2026.domain.model.InvoiceStatus

fun String.toInvoiceStatus(): InvoiceStatus {
    return when (this) {
        "Pagada" -> InvoiceStatus.Paid
        "Pendiente de Pago" -> InvoiceStatus.Pending
        "En trámite de cobro" -> InvoiceStatus.InProgress
        "Anulada" -> InvoiceStatus.Cancelled
        "Cuota Fija" -> InvoiceStatus.FixedQuota
        else -> InvoiceStatus.Pending
    }
}

