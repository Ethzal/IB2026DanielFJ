package com.iberdrola.practicas2026.presentation.composables.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.core.utils.toUiDate
import com.iberdrola.practicas2026.domain.model.Invoice
import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.ui.theme.BgPaid
import com.iberdrola.practicas2026.presentation.ui.theme.TextPaid
import com.iberdrola.practicas2026.presentation.ui.theme.BgPending
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens
import com.iberdrola.practicas2026.presentation.ui.theme.DividerColor
import com.iberdrola.practicas2026.presentation.ui.theme.TextPending
import com.iberdrola.practicas2026.presentation.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun StatusPill(status: String) {
    val (backgroundColor, textColor) = when(status) {
        "Pagada" -> Pair(BgPaid, TextPaid)
        "Pendiente de Pago" -> Pair(BgPending, TextPending)
        "En trámite de cobro" -> Pair(BgPending, TextPending)
        "Anulada" -> Pair(Color(0xFFEAEAEA), Color(0xFF757575))
        "Cuota Fija" -> Pair(Color(0xFFD4E6F1), Color(0xFF2874A6))
        else -> Pair(BgPending, TextPending)
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(Dimens.SpacingS),
        modifier = Modifier.padding(top = Dimens.SpacingS)
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = Dimens.SpacingS, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = textColor
        )
    }
}

@Composable
fun InvoiceRow(invoice: Invoice, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(top = Dimens.SpacingM)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = invoice.date.toUiDate(),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(Dimens.SpacingXS))
                Text(
                    text = invoice.type,
                    style = MaterialTheme.typography.bodySmall,
                )
                StatusPill(status = invoice.status)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${String.format(Locale.getDefault(), "%.2f", invoice.amount)} €",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal),
                    color = TextSecondary
                )
                Spacer(Modifier.width(Dimens.SpacingM))
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_info),
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(Modifier.height(Dimens.SpacingM))
    }
    HorizontalDivider(thickness = Dimens.StrokeDefault, color = DividerColor)
}