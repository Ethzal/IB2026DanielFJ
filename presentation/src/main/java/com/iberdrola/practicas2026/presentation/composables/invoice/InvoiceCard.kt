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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.domain.model.Invoice

import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.composables.common.StatusPill
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun LastInvoiceCard(invoice: Invoice) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BrandGreen),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Última factura", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Icon(
                    painter = painterResource(id = R.drawable.ic_lightbulb),
                    contentDescription = null,
                    tint = BrandGreen,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Text(text = invoice.type, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "${String.format(Locale.getDefault(), "%.2f", invoice.amount)} €",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
            )
            
            Text(
                text = "${invoice.startDate} - ${invoice.endDate}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFE0E0E0))

            StatusPill(status = invoice.status)
        }
    }
}