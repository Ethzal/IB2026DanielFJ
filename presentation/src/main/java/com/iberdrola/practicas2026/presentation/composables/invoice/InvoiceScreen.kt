package com.iberdrola.practicas2026.presentation.composables.invoice

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iberdrola.practicas2026.domain.model.Invoice
import com.iberdrola.practicas2026.domain.model.InvoiceResponse
import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.composables.common.FeedbackBottomSheet
import com.iberdrola.practicas2026.presentation.composables.common.InvoiceRow
import com.iberdrola.practicas2026.presentation.composables.common.ShimmerItem
import com.iberdrola.practicas2026.presentation.ui.invoice.InvoiceViewModel
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.TextSecondary
import com.iberdrola.practicas2026.presentation.ui.theme.TextMain
import kotlinx.coroutines.launch

@Composable
fun InvoiceScreen(
    viewModel: InvoiceViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Luz", "Gas")
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val isLocal by viewModel.usarMocksLocales.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val showFeedback by viewModel.showFeedbackSheet.collectAsStateWithLifecycle()
    val showThanks by viewModel.showThanksMessage.collectAsStateWithLifecycle()

    // Manejar botón atrás físico
    BackHandler {
        viewModel.onBackClicked(onConfirmExit = onBackClick)
    }

    if (showFeedback) {
        FeedbackBottomSheet(
            showThanks = showThanks,
            onRatingSelected = { rating -> viewModel.onRatingSelected(rating) },
            onLaterSelected = { viewModel.onLaterClicked(onBackClick) },
            onDismiss = { viewModel.onSheetDismissed(onBackClick) }
        )
    }

    var showNotAvailableDialog by remember { mutableStateOf(false) }

    if (showNotAvailableDialog) {
        AlertDialog(
            onDismissRequest = { showNotAvailableDialog = false },
            title = { Text("Información") },
            text = { Text("Esta factura aún no está disponible para su visualización.") },
            confirmButton = {
                TextButton(onClick = { showNotAvailableDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            InvoiceHeader(
                isLocal = isLocal,
                onBack = onBackClick,
                onModeChange = { isLocal ->
                    viewModel.toggleMode(isLocal)
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            if (isLocal) context.getString(R.string.modo_local_cargando_mocks)
                            else context.getString(R.string.modo_remoto_conectando_a_api)
                        )
                    }
                }
            )
        },
        containerColor = Color.White,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = BrandGreen.copy(alpha = 0.9f),
                        contentColor = Color.White
                    )
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            
            // TABS (Luz / Gas)
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = BrandGreen,
                edgePadding = 16.dp,
                divider = {
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                },
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = BrandGreen
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                            val filterType = if (index == 0) "Factura Luz" else "Factura Gas"
                            viewModel.filterInvoicesByType(filterType)
                        },
                        text = { Text(title, color = if (selectedTabIndex == index) TextMain else TextSecondary) }
                    )
                }
            }

            // GESTIÓN DE ESTADOS (Loading, Success, Error)
            when (val state = uiState) {
                is InvoiceViewModel.UiState.Loading -> {
                    Column { repeat(3) { ShimmerItem() } }
                }

                is InvoiceViewModel.UiState.Success -> {
                    InvoiceList(
                        data = state.data,
                        onInvoiceClick = { showNotAvailableDialog = true }
                    )
                }

                is InvoiceViewModel.UiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.msg, color = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun InvoiceList(data: InvoiceResponse, onInvoiceClick: (Invoice) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Tarjeta principal
        item {
            data.lastInvoice?.let { last ->
                LastInvoiceCard(invoice = last)
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Histórico de facturas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                FilterButton(onClick = { /* ... */ })
            }
        }

        // Título del año
        item {
            Text(
                text = "2024",
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // Listado plano con separadores
        items(data.history) { invoice ->
            InvoiceRow(invoice = invoice, onClick = { onInvoiceClick(invoice) })
        }
    }
}