package com.iberdrola.practicas2026.presentation.composables.invoice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.domain.model.InvoiceItem
import com.iberdrola.practicas2026.presentation.composables.common.StatusPill
import com.iberdrola.practicas2026.presentation.ui.theme.*
import java.util.Locale

@Composable
fun InvoiceCard(
    invoice: InvoiceItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(Dimens.CornerDefault),
        border = BorderStroke(Dimens.StrokeDefault, BrandGreen),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = invoice.date,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = invoice.type,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                StatusPill(status = invoice.status)
            }

            Text(
                text = "${String.format(Locale.getDefault(), "%.2f", invoice.amount)} €",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}