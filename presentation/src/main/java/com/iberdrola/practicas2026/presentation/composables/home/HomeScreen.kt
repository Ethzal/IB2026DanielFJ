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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.ui.home.MainViewModel
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens

@Composable
fun HomeScreen(
    onNavigateToInvoices: () -> Unit,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val isLocal by mainViewModel.isLocalMode.collectAsStateWithLifecycle()
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
                        RoundedCornerShape(bottomStart = Dimens.SpacingXL, bottomEnd = Dimens.SpacingXL)
                    )
                    .padding(Dimens.SpacingL)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.hola_daniel), color = Color.White, style = MaterialTheme.typography.headlineMedium)
                        Spacer(Modifier.weight(1f))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isLocal) "Local" else "Remoto",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall
                            )
                            androidx.compose.material3.Switch(
                                checked = isLocal,
                                onCheckedChange = { mainViewModel.toggleMode(it) },
                                colors = androidx.compose.material3.SwitchDefaults.colors(
                                    checkedThumbColor = BrandGreen,
                                    checkedTrackColor = Color.White,
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = Color.White.copy(alpha = 0.4f)
                                )
                            )
                        }

                        Spacer(Modifier.width(12.dp))
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
                    .padding(Dimens.SpacingM)
                    .offset(y = (-40).dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                shape = RoundedCornerShape(Dimens.CornerDefault)
            ) {
                Row(modifier = Modifier.padding(Dimens.SpacingM), verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.ic_lightbulb), contentDescription = null, tint = BrandGreen, modifier = Modifier.size(40.dp))
                    Spacer(Modifier.width(Dimens.SpacingM))
                    Column {
                        Text(stringResource(R.string.factura_digital_activa), fontWeight = FontWeight.Bold, color = BrandGreen)
                        Text(stringResource(R.string.ya_estas_ahorrando), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // Mi Energía
            Text(stringResource(R.string.mi_energia), modifier = Modifier.padding(horizontal = Dimens.SpacingM), style = MaterialTheme.typography.titleLarge)
            
            Row(modifier = Modifier
                .padding(Dimens.SpacingM)
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
                Spacer(Modifier.width(Dimens.SpacingM))
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
        modifier = modifier.height(Dimens.ShimmerTitleWidth),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(Dimens.SpacingXS)
    ) {
        Column(Modifier.padding(Dimens.SpacingM)) {
            Icon(painterResource(icon), contentDescription = null, tint = BrandGreen)
            Spacer(Modifier.weight(1f))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}