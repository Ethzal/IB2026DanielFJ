package com.iberdrola.practicas2026.presentation.composables.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens
import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.ui.theme.TextMain

@Composable
fun InlineNotice(
    message: String,
    isError: Boolean = false,
    onClose: () -> Unit
) {
    Surface(
        color = if (isError) Color(0xFFFADBD8) else Color(0xFFB1DEC8),
        modifier = Modifier
            .fillMaxWidth()
            .padding()
            .layout { measurable, constraints ->
                val paddingPx = 16.dp.roundToPx()
                val extendedWidth = constraints.maxWidth + (paddingPx * 2)
                val placeable = measurable.measure(
                    constraints.copy(minWidth = extendedWidth, maxWidth = extendedWidth)
                )
                layout(constraints.maxWidth, placeable.height) {
                    placeable.place(-paddingPx, 0)
                }
            }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Dimens.SpacingS, vertical = Dimens.SpacingM),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = if (isError) R.drawable.ic_error_connection else R.drawable.ic_check),
                contentDescription = null,
                tint = if (isError) Color(0xFFB03A2E) else BrandGreen,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(Dimens.SpacingS))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = if (isError) Color(0xFFB03A2E) else TextMain,
                modifier = Modifier.padding(top = 1.dp)
            )
            IconButton(onClick = onClose, modifier = Modifier.size(24.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "Cerrar aviso",
                    tint = if (isError) Color(0xFFB03A2E) else TextMain,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}