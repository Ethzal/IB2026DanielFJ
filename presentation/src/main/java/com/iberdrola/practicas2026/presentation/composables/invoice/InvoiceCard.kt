package com.iberdrola.practicas2026.presentation.composables.invoice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.iberdrola.practicas2026.core.utils.toLastInvoiceDate
import com.iberdrola.practicas2026.domain.model.Invoice

import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.composables.common.StatusPill
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens
import com.iberdrola.practicas2026.presentation.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun LastInvoiceCard(
    invoice: Invoice,
    onClick: () -> Unit
) {

    val icon = if (invoice.type.contains("Gas", ignoreCase = true)) {
        R.drawable.ic_gas
    } else {
        R.drawable.ic_lightbulb
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimens.SpacingXL),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(Dimens.StrokeDefault, BrandGreen),
        shape = RoundedCornerShape(Dimens.CornerDefault)
    ) {
        Column(modifier = Modifier.padding(Dimens.SpacingM)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Last Invoice
                    Text(
                        text = stringResource(R.string.ultima_factura),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    // Invoice type
                    Text(
                        text = invoice.type,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = BrandGreen,
                    modifier = Modifier.size(Dimens.IconM)
                )
            }
            
            Spacer(modifier = Modifier.height(Dimens.SpacingS))

            // Amount
            Text(
                text = "${String.format(Locale.getDefault(), "%.2f", invoice.amount)} €",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
            )

            // Date range
            Text(
                text = "${invoice.startDate.toLastInvoiceDate()} - ${invoice.endDate.toLastInvoiceDate()}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            
            HorizontalDivider(modifier = Modifier.padding(top = Dimens.SpacingM, bottom = Dimens.SpacingS), color = Color(0xFFE0E0E0))

            StatusPill(status = invoice.status)
        }
    }
}