package com.iberdrola.practicas2026.domain.model

sealed class InvoiceStatus(val id: String) {
    data object Paid : InvoiceStatus("Pagada")
    data object Pending : InvoiceStatus("Pendiente de Pago")
    data object InProgress : InvoiceStatus("En trámite de cobro")
    data object Cancelled : InvoiceStatus("Anulada")
    data object FixedQuota : InvoiceStatus("Cuota Fija")

    companion object {
        val all = listOf(Paid, Pending, InProgress, Cancelled, FixedQuota)

        fun fromId(id: String?): InvoiceStatus {
            return all.find { it.id == id } ?: Pending // Si es nulo o no existe, por defecto Pendiente
        }
    }
}