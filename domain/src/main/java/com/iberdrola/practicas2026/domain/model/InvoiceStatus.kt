package com.iberdrola.practicas2026.domain.model

sealed class InvoiceStatus(val id: String) {
    data object Paid : InvoiceStatus("Pagada")
    data object Pending : InvoiceStatus("Pendiente de Pago")
    data object InProgress : InvoiceStatus("En trámite de cobro")
    data object Cancelled : InvoiceStatus("Anulada")
    data object FixedQuota : InvoiceStatus("Cuota Fija")

    companion object {
        val all get() = listOf(Paid, Pending, InProgress, Cancelled, FixedQuota)

        fun fromId(id: String?): InvoiceStatus {
            return when (id) {
                "Pagada" -> Paid
                "Pendiente de Pago" -> Pending
                "En trámite de cobro" -> InProgress
                "Anulada" -> Cancelled
                "Cuota Fija" -> FixedQuota
                else -> Pending
            }
        }
    }
}