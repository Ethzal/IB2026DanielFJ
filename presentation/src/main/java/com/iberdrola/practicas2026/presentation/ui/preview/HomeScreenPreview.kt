package com.iberdrola.practicas2026.presentation.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.domain.model.Invoice
import com.iberdrola.practicas2026.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.presentation.composables.home.HomeScreenContent
import com.iberdrola.practicas2026.presentation.ui.home.InvoiceState
import com.iberdrola.practicas2026.presentation.ui.theme.EnergyAppTheme

@Preview(showBackground = true, name = "Home - Cargando")
@Composable
fun HomeScreenLoadingPreview() {
    EnergyAppTheme {
        HomeScreenContent(
            isLocal = true,
            lastInvoiceState = InvoiceState.Loading,
            onToggleMode = {},
            onNavigateToInvoices = {},
            onNavigateToElectronicInvoice = {}
        )
    }
}

@Preview(showBackground = true, name = "Home - Factura Cargada")
@Composable
fun HomeScreenSuccessPreview() {
    EnergyAppTheme {
        val fakeInvoice = Invoice(
            id = "1",
            date = "2024-03-08",
            type = "Factura Luz",
            amount = 32.21,
            status = InvoiceStatus.Paid
        )

        HomeScreenContent(
            isLocal = false,
            lastInvoiceState = InvoiceState.Success(fakeInvoice),
            onToggleMode = {},
            onNavigateToInvoices = {},
            onNavigateToElectronicInvoice = {}
        )
    }
}