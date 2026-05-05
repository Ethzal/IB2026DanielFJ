package com.iberdrola.practicas2026.presentation.mapper

import com.iberdrola.practicas2026.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.presentation.composables.common.ContractStatus
import com.iberdrola.practicas2026.presentation.composables.common.StatusStyle
import com.iberdrola.practicas2026.presentation.composables.common.StatusUiModel

fun InvoiceStatus?.toUiModel(usePlural: Boolean = false): StatusUiModel {
    if (this == null) {
        return StatusUiModel(
            label = "Desconocido",
            style = StatusStyle.NEUTRAL
        )
    }
    return StatusUiModel(
        label = when (this) {
            InvoiceStatus.Paid -> if (usePlural) "Pagadas" else "Pagada"
            InvoiceStatus.Pending -> if (usePlural) "Pendientes de pago" else "Pendiente de pago"
            InvoiceStatus.InProgress -> if (usePlural) "En trámite de cobro" else "En trámite de cobro"
            InvoiceStatus.Cancelled -> if (usePlural) "Anuladas" else "Anulada"
            InvoiceStatus.FixedQuota -> if (usePlural) "Cuotas fijas" else "Cuota fija"
        },
        style = when (this) {
            InvoiceStatus.Paid -> StatusStyle.SUCCESS
            InvoiceStatus.Pending,
            InvoiceStatus.InProgress -> StatusStyle.WARNING
            InvoiceStatus.Cancelled -> StatusStyle.NEUTRAL
            InvoiceStatus.FixedQuota -> StatusStyle.INFO
        }
    )
}
fun ContractStatus.toUiModel(): StatusUiModel {
    return StatusUiModel(
        label = when (this) {
            ContractStatus.Active -> "Activa"
            ContractStatus.Inactive -> "Sin Activar"
        },
        style = when (this) {
            ContractStatus.Active -> StatusStyle.SUCCESS
            ContractStatus.Inactive -> StatusStyle.NEUTRAL
        }
    )
}