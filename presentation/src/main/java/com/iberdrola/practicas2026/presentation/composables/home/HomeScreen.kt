package com.iberdrola.practicas2026.presentation.composables.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen

@Composable
fun HomeScreen(onNavigateToInvoices: () -> Unit ) {
    Scaffold { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        BrandGreen,
                        RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.hola_daniel), color = Color.White, style = MaterialTheme.typography.headlineMedium)
                        Spacer(Modifier.weight(1f))
                        // Profile Icon
                        Box(Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                        }
                    }
                    Text(stringResource(R.string.c_miguel_de_cervantes_47), color = Color.White.copy(alpha = 0.8f))
                }
            }

            // Info Card
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .offset(y = (-40).dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.ic_lightbulb), contentDescription = null, tint = BrandGreen, modifier = Modifier.size(40.dp))
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(stringResource(R.string.factura_digital_activa), fontWeight = FontWeight.Bold, color = BrandGreen)
                        Text(stringResource(R.string.ya_estas_ahorrando), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // Mi Energía
            Text(stringResource(R.string.mi_energia), modifier = Modifier.padding(horizontal = 16.dp), style = MaterialTheme.typography.titleLarge)
            
            Row(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()) {
                // Invoice Card
                EnergyCard(
                    title = stringResource(R.string.mis_facturas),
                    subtitle = stringResource(R.string.ultima_factura),
                    value = stringResource(R.string._32_21),
                    icon = R.drawable.ic_lightbulb,
                    onClick = onNavigateToInvoices,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(16.dp))
                // Empty Card
                Box(Modifier.weight(1f)) 
            }
        }
    }
}

@Composable
fun EnergyCard(title: String, subtitle: String, value: String, icon: Int, onClick: () -> Unit, modifier: Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.height(150.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Icon(painterResource(icon), contentDescription = null, tint = BrandGreen)
            Spacer(Modifier.weight(1f))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}