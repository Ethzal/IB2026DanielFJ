package com.iberdrola.practicas2026.presentation.composables.invoice

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.iberdrola.practicas2026.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.presentation.R

@Composable
fun InvoiceStatus.getLabel(): String {
    return when (this) {
        InvoiceStatus.Paid -> stringResource(R.string.status_paid)
        InvoiceStatus.Pending -> stringResource(R.string.status_pending)
        InvoiceStatus.InProgress -> stringResource(R.string.status_in_progress)
        InvoiceStatus.Cancelled -> stringResource(R.string.status_cancelled)
        InvoiceStatus.FixedQuota -> stringResource(R.string.status_fixed_quota)
    }
}