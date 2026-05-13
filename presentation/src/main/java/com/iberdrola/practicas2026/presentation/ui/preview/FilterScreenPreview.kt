package com.iberdrola.practicas2026.presentation.ui.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.domain.model.InvoiceFilter
import com.iberdrola.practicas2026.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.presentation.composables.invoice.FilterScreen
import com.iberdrola.practicas2026.presentation.ui.theme.EnergyAppTheme

@Preview(
    showBackground = true,
    name = "Pantalla Filtros - Estado Inicial",
    showSystemUi = true
)
@Composable
fun FilterScreenPreview() {
    EnergyAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            FilterScreen(
                currentFilter = InvoiceFilter(
                    dateFrom = null,
                    dateTo = null,
                    amountRange = 0f..500f,
                    statuses = emptySet()
                ),
                amountBounds = 0f..500f,
                minDateAllowed = null,
                onFilterStateChanged = { range, statuses ->
                },
                onApplyFilters = {},
                onClearFilters = {},
                onBack = {}
            )
        }
    }
}

@Preview(
    showBackground = true,
    name = "Pantalla Filtros - Con Datos Seleccionados",
    showSystemUi = true
)
@Composable
fun FilterScreenSelectedPreview() {
    EnergyAppTheme {
        FilterScreen(
            currentFilter = InvoiceFilter(
                dateFrom = "2023-01-01",
                dateTo = "2023-12-31",
                amountRange = 100f..400f,
                statuses = setOf(InvoiceStatus.Paid, InvoiceStatus.Pending)
            ),
            amountBounds = 0f..500f,
            minDateAllowed = 1672531200000L,
            onFilterStateChanged = { _, _ -> },
            onApplyFilters = {},
            onClearFilters = {},
            onBack = {}
        )
    }
}