package com.iberdrola.practicas2026.presentation.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.domain.model.Invoice
import com.iberdrola.practicas2026.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.presentation.composables.invoice.LastInvoiceCard
import com.iberdrola.practicas2026.presentation.ui.theme.EnergyAppTheme

@Preview(showBackground = true)
@Composable
fun LastInvoiceCardPreview() {

    EnergyAppTheme{
        val fakeInvoice = Invoice(
            type = "Factura Luz",
            amount = 20.00,
            startDate = "2024-02-01",
            endDate = "2024-03-04",
            status = InvoiceStatus.Paid,
            id = "3",
            date = "2024-03-04"
        )

        LastInvoiceCard(
            invoice = fakeInvoice,
            onClick = {}
        )
    }
}