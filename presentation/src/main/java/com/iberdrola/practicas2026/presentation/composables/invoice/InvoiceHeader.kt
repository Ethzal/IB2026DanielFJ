package com.iberdrola.practicas2026.presentation.composables.invoice

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.TextMain
import com.iberdrola.practicas2026.presentation.ui.theme.Gray


@Composable
fun InvoiceHeader(isLocal: Boolean, onBack: () -> Unit, onModeChange: (Boolean) -> Unit) {

    val switchColors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = Gray,
        uncheckedThumbColor = BrandGreen,
        uncheckedTrackColor = Color(0xFFA9DFBF)
    )

    Column(modifier = Modifier.padding(16.dp).padding(top = 20.dp, start = 0.dp, end = 0.dp, bottom = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onBack() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = BrandGreen,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Atrás",
                    color = BrandGreen,
                    style = MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.Underline)
                )
            }

            // Switch
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isLocal) "Local" else "Remoto",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isLocal) Color.Gray else BrandGreen
                )
                Spacer(Modifier.width(8.dp))
                Switch(
                    checked = isLocal,
                    onCheckedChange = { onModeChange(it) },
                    colors = switchColors
                )
            }
        }

        Spacer(Modifier.height(8.dp))
        Text(text = "Mis facturas", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(text = "C/ PALMA - ARTA KM 49, 5 , 4ºA -PINTO - MADRID", style = MaterialTheme.typography.bodyLarge, color = TextMain, fontWeight = FontWeight.Bold)
    }
}