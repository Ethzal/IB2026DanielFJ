package com.iberdrola.practicas2026.presentation.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.presentation.ui.theme.EnergyAppTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.presentation.composables.common.ShimmerFilterButton
import com.iberdrola.practicas2026.presentation.composables.common.ShimmerInvoiceRow
import com.iberdrola.practicas2026.presentation.composables.common.ShimmerLastInvoiceCard
import com.iberdrola.practicas2026.presentation.composables.common.ShimmerYearHeader
import com.iberdrola.practicas2026.presentation.composables.common.rememberShimmerBrush
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens

@Preview(
    name = "Invoice Loading Shimmer",
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
fun InvoiceLoadingShimmerPreview() {
    EnergyAppTheme {
        val brush = rememberShimmerBrush()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(Dimens.SpacingM)
        ) {
            // Tarjeta principal
            item { ShimmerLastInvoiceCard(brush) }

            // Header + botón filtro
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.SpacingM, bottom = Dimens.SpacingM),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .width(180.dp)
                            .height(24.dp)
                            .background(brush)
                    )

                    ShimmerFilterButton(brush)
                }
            }

            // Año
            item { ShimmerYearHeader(brush) }

            // Lista
            items(5) {
                ShimmerInvoiceRow(brush)
            }
        }
    }
}