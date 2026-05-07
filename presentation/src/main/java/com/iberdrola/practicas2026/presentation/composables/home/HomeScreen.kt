package com.iberdrola.practicas2026.presentation.composables.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iberdrola.practicas2026.core.utils.formatSpanishCurrency
import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.composables.common.rememberShimmerBrush
import com.iberdrola.practicas2026.presentation.ui.home.InvoiceState
import com.iberdrola.practicas2026.presentation.ui.home.MainViewModel
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreenLight
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens
import com.iberdrola.practicas2026.presentation.ui.theme.TextMain

@Composable
fun HomeScreen(
    onNavigateToInvoices: () -> Unit,
    onNavigateToElectronicInvoice: () -> Unit,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val isLocal by mainViewModel.isLocalMode.collectAsStateWithLifecycle()
    val lastInvoiceState by mainViewModel.lastInvoiceState.collectAsStateWithLifecycle()

    HomeScreenContent(
        isLocal = isLocal,
        lastInvoiceState = lastInvoiceState,
        onToggleMode = { mainViewModel.toggleMode(it) },
        onNavigateToInvoices = onNavigateToInvoices,
        onNavigateToElectronicInvoice = onNavigateToElectronicInvoice
    )
}

@Composable
fun HomeScreenContent(
    isLocal: Boolean,
    lastInvoiceState: InvoiceState,
    onToggleMode: (Boolean) -> Unit,
    onNavigateToInvoices: () -> Unit,
    onNavigateToElectronicInvoice: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val cardWidth = (configuration.screenWidthDp / 3f).dp

    Scaffold(containerColor = Color.White) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = BrandGreen,
                            shape = RoundedCornerShape(bottomStart = Dimens.CornerButton)
                        )
                        .padding(top = Dimens.SpacingM, bottom = Dimens.SpacingL)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = Dimens.SpacingL, end = Dimens.SpacingM),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(Modifier.height(Dimens.SpacingS))
                            Text(
                                text = if (isLocal) stringResource(R.string.local) else stringResource(
                                    R.string.remoto
                                ),
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall
                            )
                            Switch(
                                checked = isLocal,
                                onCheckedChange = onToggleMode,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = BrandGreen,
                                    checkedTrackColor = Color.White,
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = Color.White.copy(alpha = 0.4f)
                                )
                            )
                        }

                        Spacer(Modifier.width(Dimens.SpacingM))
                        // Profile Icon
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                tint = BrandGreen,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(Dimens.SpacingM))

                    Text(
                        text = stringResource(R.string.hola_daniel),
                        color = Color.White,
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.padding(horizontal = Dimens.SpacingM)
                    )

                    Spacer(Modifier.height(Dimens.SpacingXS))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = Dimens.SpacingM)
                    ) {
                        Text(
                            text = stringResource(R.string.direccion),
                            color = BrandGreenLight,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.width(Dimens.SpacingXS))
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null,
                            tint = BrandGreenLight,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(Modifier.height(Dimens.SpacingM))

                    // Info Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.SpacingM),
                        colors = CardDefaults.cardColors(containerColor = BrandGreenLight),
                        shape = RoundedCornerShape(Dimens.CornerDefault)
                    ) {
                        Row(modifier = Modifier.padding(Dimens.SpacingM).fillMaxWidth()) {
                            Icon(
                                painter = painterResource(R.drawable.ic_electronic_invoice),
                                contentDescription = null,
                                tint = BrandGreen,
                                modifier = Modifier.size(Dimens.IconM)
                            )
                            Spacer(Modifier.width(Dimens.SpacingM))
                            Column {
                                Text(
                                    text = stringResource(R.string.factura_digital_activa),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(Modifier.height(Dimens.SpacingXS))
                                Text(
                                    text = stringResource(R.string.ya_estas_ahorrando),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMain
                                )
                                Spacer(Modifier.height(Dimens.SpacingS))
                                Text(
                                    text = "Modificar email",
                                    color = BrandGreen,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.Underline),
                                    modifier = Modifier.clickable { onNavigateToElectronicInvoice() }
                                )
                            }
                        }
                    }
                }
            }

            // Body
            Box(modifier = Modifier.fillMaxWidth().background(BrandGreen)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topEnd = Dimens.CornerButton)
                        )
                        .padding(top = Dimens.SpacingL)
                ) {

                    // Mi Energía
                    Text(
                        text = stringResource(R.string.mi_energia),
                        modifier = Modifier.padding(horizontal = Dimens.SpacingM),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(Dimens.SpacingM))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = Dimens.SpacingM),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingM)
                    ) {
                        item {
                            LastInvoiceHomeCard(
                                state = lastInvoiceState,
                                width = cardWidth,
                                onClick = onNavigateToInvoices
                            )
                        }
                    }

                    Spacer(Modifier.height(Dimens.SpacingL))

                    // Mis accesos
                    Text(
                        text = "Mis accesos",
                        modifier = Modifier.padding(horizontal = Dimens.SpacingM),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(Dimens.SpacingM))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = Dimens.SpacingM),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingM)
                    ) {
                        item {
                            ElectronicInvoiceHomeCard(
                                width = cardWidth,
                                onClick = onNavigateToElectronicInvoice
                            )
                        }
                    }

                    Spacer(Modifier.height(Dimens.SpacingXL))
                }
            }
        }
    }
}

@Composable
fun LastInvoiceHomeCard(state: InvoiceState, width: Dp, onClick: () -> Unit) {

    val isLoading = state is InvoiceState.Loading

    Card(
        onClick = onClick,
        modifier = Modifier.width(width).height(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            disabledContainerColor = Color.White
        ),
        enabled = !isLoading,
        border = BorderStroke(Dimens.StrokeDefault, BrandGreen),
        elevation = CardDefaults.cardElevation(Dimens.SpacingXS)
    ) {
        when (state) {
            is InvoiceState.Loading -> {
                val brush = rememberShimmerBrush()
                Column(Modifier.padding(Dimens.SpacingM).fillMaxSize()) {
                    Box(Modifier.size(32.dp).background(brush, CircleShape))
                    Spacer(Modifier.height(Dimens.SpacingL))
                    Box(Modifier.height(16.dp).fillMaxWidth(0.8f).background(brush))
                    Spacer(Modifier.height(Dimens.SpacingS))
                    Box(Modifier.height(12.dp).fillMaxWidth(0.6f).background(brush))
                    Spacer(Modifier.height(4.dp))
                    Box(Modifier.height(12.dp).fillMaxWidth(0.4f).background(brush))
                }
            }

            is InvoiceState.Success, is InvoiceState.Error -> {

                var isGas = false
                var amountText = "— €"
                var typeText = "—"

                if (state is InvoiceState.Success && state.invoice != null) {
                    val invoice = state.invoice
                    isGas = invoice.type.contains("Gas", ignoreCase = true)
                    amountText = invoice.amount.formatSpanishCurrency() + " €"
                    typeText = invoice.type.replace("Factura ", "")
                }

                Column(Modifier.padding(Dimens.SpacingM).fillMaxSize()) {
                    Icon(
                        painter = painterResource(if (isGas) R.drawable.ic_gas else R.drawable.ic_lightbulb),
                        contentDescription = null,
                        tint = if (state is InvoiceState.Error || (state is InvoiceState.Success && state.invoice == null))
                            BrandGreen.copy(alpha = 0.5f) else BrandGreen,
                        modifier = Modifier.size(Dimens.IconXM)
                    )

                    Spacer(Modifier.height(Dimens.SpacingL))

                    Text(
                        text = amountText,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(Dimens.SpacingS))

                    Text(
                        text = stringResource(R.string.ultima_factura),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMain
                    )

                    Spacer(Modifier.height(Dimens.SpacingXS))

                    Text(
                        text = typeText,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMain
                    )
                }
            }
        }
    }
}

@Composable
fun ElectronicInvoiceHomeCard(width: Dp, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(width).height(180.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(Dimens.StrokeDefault, BrandGreen),
        elevation = CardDefaults.cardElevation(Dimens.SpacingXS)
    ) {
        Column(Modifier.padding(Dimens.SpacingM).fillMaxSize()) {
            Icon(
                painter = painterResource(R.drawable.ic_electronic_invoice),
                contentDescription = null,
                tint = BrandGreen,
                modifier = Modifier.size(Dimens.IconXM)
            )
            Spacer(Modifier.height(Dimens.SpacingL))
            Text(
                text = stringResource(R.string.factura_electronica),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}