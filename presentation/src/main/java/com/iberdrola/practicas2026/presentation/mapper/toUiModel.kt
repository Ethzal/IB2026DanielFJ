package com.iberdrola.practicas2026.presentation.mapper

import com.iberdrola.practicas2026.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.presentation.composables.common.ContractStatus
import com.iberdrola.practicas2026.presentation.composables.common.StatusStyle
import com.iberdrola.practicas2026.presentation.composables.common.StatusUiModel

fun InvoiceStatus?.toUiModel(): StatusUiModel {
    if (this == null) {
        return StatusUiModel(
            label = "Desconocido",
            style = StatusStyle.NEUTRAL
        )
    }
    return StatusUiModel(
        label = when (this) {
            InvoiceStatus.Paid -> "Pagada"
            InvoiceStatus.Pending -> "Pendiente de Pago"
            InvoiceStatus.InProgress -> "En trámite de cobro"
            InvoiceStatus.Cancelled -> "Anulada"
            InvoiceStatus.FixedQuota -> "Cuota Fija"
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