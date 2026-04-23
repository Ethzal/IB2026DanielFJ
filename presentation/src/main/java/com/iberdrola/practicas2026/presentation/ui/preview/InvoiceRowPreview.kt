package com.iberdrola.practicas2026.presentation.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.domain.model.Invoice
import com.iberdrola.practicas2026.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.presentation.composables.common.InvoiceRow
import com.iberdrola.practicas2026.presentation.ui.theme.EnergyAppTheme

@Preview(showBackground = true)
@Composable
fun InvoiceRowPreview() {

    EnergyAppTheme {
        val fakeInvoice = Invoice(
            date = "2024-03-08",
            type = "Factura Luz",
            amount = 20.00,
            status = InvoiceStatus.Paid,
            id = "2",
            startDate = "2024-02-08",
            endDate = "2024-03-08"
        )

        InvoiceRow(
            invoice = fakeInvoice,
            onClick = {}
        )
    }
}