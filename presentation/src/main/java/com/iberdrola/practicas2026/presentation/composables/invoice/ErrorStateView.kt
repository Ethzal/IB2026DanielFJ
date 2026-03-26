package com.iberdrola.practicas2026.presentation.composables.invoice

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens
import com.iberdrola.practicas2026.presentation.ui.theme.TextSecondary
import com.iberdrola.practicas2026.presentation.ui.theme.White

@Composable
fun ErrorStateView(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.SpacingXL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_error_connection),
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color(0xFFB03A2E)
        )
        Spacer(Modifier.height(Dimens.SpacingM))
        Text(stringResource(R.string.ups_algo_ha_ido_mal), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(Dimens.SpacingS))
        Text(message, style = MaterialTheme.typography.bodyMedium, color = TextSecondary, textAlign = TextAlign.Center)
        Spacer(Modifier.height(Dimens.SpacingL))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.reintentar_conexion), color = White)
        }
    }
}