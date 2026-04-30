package com.iberdrola.practicas2026.presentation.composables.invoice

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iberdrola.practicas2026.domain.model.Invoice
import com.iberdrola.practicas2026.domain.model.InvoiceResponse
import com.iberdrola.practicas2026.domain.model.InvoiceType
import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.composables.common.FeedbackBottomSheet
import com.iberdrola.practicas2026.presentation.composables.common.InvoiceRow
import com.iberdrola.practicas2026.presentation.composables.common.ShimmerFilterButton
import com.iberdrola.practicas2026.presentation.composables.common.ShimmerInvoiceRow
import com.iberdrola.practicas2026.presentation.composables.common.ShimmerLastInvoiceCard
import com.iberdrola.practicas2026.presentation.composables.common.ShimmerYearHeader
import com.iberdrola.practicas2026.presentation.composables.common.SlidingTabsSection
import com.iberdrola.practicas2026.presentation.composables.common.rememberShimmerBrush
import com.iberdrola.practicas2026.presentation.ui.invoice.InvoiceEvent
import com.iberdrola.practicas2026.presentation.ui.invoice.InvoiceViewModel
import com.iberdrola.practicas2026.presentation.ui.theme.DarkGray
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens
import com.iberdrola.practicas2026.presentation.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun InvoiceScreen(
    viewModel: InvoiceViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val tabs = listOf(stringResource(R.string.luz), stringResource(R.string.gas))
    val snackbarHostState = remember { SnackbarHostState() }

    val showFeedback by viewModel.showFeedbackSheet.collectAsStateWithLifecycle()
    val showThanks by viewModel.showThanksMessage.collectAsStateWithLifecycle()

    // FILTROS
    val invoiceFilter by viewModel.invoiceFilter.collectAsStateWithLifecycle()
    val amountBounds by viewModel.amountBounds.collectAsStateWithLifecycle()
    var showFilterScreen by remember { mutableStateOf(false) }
    val isFiltering by viewModel.isFiltering.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    LaunchedEffect(pagerState.currentPage) {
        viewModel.updateCurrentTab(pagerState.currentPage)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is InvoiceEvent.SwitchToTab -> {
                    pagerState.animateScrollToPage(event.index)
                }
            }
        }
    }

    fun showSnackbar(message: String) {
        coroutineScope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    // Manejar botón atrás físico
    BackHandler {
        if (showFilterScreen) {
            showFilterScreen = false
        } else {
            viewModel.onBackClicked(onConfirmExit = onBackClick)
        }
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
            title = { Text(stringResource(R.string.informacion)) },
            text = { Text(stringResource(R.string.factura_no_disponible), fontWeight = FontWeight.Normal) },
            confirmButton = {
                TextButton(onClick = { showNotAvailableDialog = false }) {
                    Text(stringResource(R.string.aceptar))
                }
            },
            containerColor = Color.White
        )
    }

    if (showFilterScreen) {
        FilterScreen(
            currentFilter = invoiceFilter,
            amountBounds = amountBounds,
            onApplyFilters = {
                viewModel.applyFilters(it)
                val count = viewModel.getResultCount()
                showFilterScreen = false
                val message = context.resources.getQuantityString(R.plurals.filtros_aplicados, count, count)
                showSnackbar(message)
            },
            onClearFilters = {
                viewModel.clearFilters()
            },
            onBack = { showFilterScreen = false }
        )
    } else {
        Scaffold(
            topBar = {
                InvoiceHeader(
                    onBack = { viewModel.onBackClicked(onConfirmExit = onBackClick) }
                )
            },
            containerColor = Color.White,
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = { data ->
                        Snackbar(
                            snackbarData = data,
                            containerColor = DarkGray.copy(alpha = 0.9f),
                            contentColor = White
                        )
                    }
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {

                // TABS (Luz / Gas)
                SlidingTabsSection(
                    pagerState = pagerState,
                    tabs = tabs, // Tu lista de nombres ["Luz", "Gas"]
                    coroutineScope = coroutineScope
                )

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    userScrollEnabled = true,
                    beyondViewportPageCount = 1
                ) { pageIndex ->

                    val type = if (pageIndex == 0) InvoiceType.LIGHT else InvoiceType.GAS
                    val uiStatesMap by viewModel.uiStates.collectAsStateWithLifecycle()

                    // Obtenemos el estado específico de esta página
                    val pageUiState = uiStatesMap[type] ?: InvoiceViewModel.UiState.Loading

                    LaunchedEffect(type) {
                        viewModel.filterInvoices(type)
                    }

                    when (pageUiState) {
                        is InvoiceViewModel.UiState.Loading -> {
                            val brush = rememberShimmerBrush()
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(Dimens.SpacingM)
                            ) {
                                // Tarjeta principal destacada
                                item { ShimmerLastInvoiceCard(brush) }

                                // Título Histórico + Botón Filtrar
                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = Dimens.SpacingM, bottom = Dimens.SpacingM),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Caja para "Histórico de facturas"
                                        Box(modifier = Modifier.width(180.dp).height(24.dp).background(brush))

                                        // Botón de filtrar
                                        ShimmerFilterButton(brush)
                                    }
                                }

                                // Año
                                item { ShimmerYearHeader(brush) }

                                // Lista de facturas del histórico
                                items(5) {
                                    ShimmerInvoiceRow(brush)
                                }
                            }
                        }
                        is InvoiceViewModel.UiState.Success -> {
                            InvoiceList(
                                data = pageUiState.data,
                                onInvoiceClick = { showNotAvailableDialog = true },
                                onFilterClick = { showFilterScreen = true },
                                isFiltering = isFiltering,
                                onClearFilters = { viewModel.clearFilters() }
                            )
                        }

                        is InvoiceViewModel.UiState.Error -> {
                            ErrorStateView(
                                message = pageUiState.msg,
                                onRetry = { viewModel.fetchFacturas(isLocal = false) } // Fuerza la recarga
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InvoiceList(
    data: InvoiceResponse,
    onInvoiceClick: (Invoice) -> Unit,
    onFilterClick: () -> Unit,
    onClearFilters: () -> Unit,
    isFiltering: Boolean
) {
    val groupedHistory = data.history.groupBy {
        it.date.take(4)
    }

    val hasContent = data.lastInvoice != null || data.history.isNotEmpty()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = Dimens.SpacingM)
    ) {
        // Tarjeta principal
        if (data.allInvoices.isNotEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.SpacingM)
                ) {
                    data.lastInvoice?.let { last ->
                        LastInvoiceCard(
                            invoice = last,
                            onClick = { onInvoiceClick(last) }
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.SpacingM),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.historico_de_facturas),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                FilterButton(onClick = onFilterClick, isFilterActive = isFiltering)
            }
        }

        if (data.allInvoices.isEmpty() || !hasContent) {
            item {
                EmptyStateView(
                    iconRes = R.drawable.ic_energy_empty,
                    title = stringResource(R.string.sin_facturas),
                    message = stringResource(R.string.no_hemos_encontrado_facturas),
                    onClearFilters = onClearFilters
                )
            }
        } else {
            groupedHistory.forEach { (year, invoices) ->
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.SpacingM)
                    ) {
                    Text(
                        text = year,
                        modifier = Modifier.padding(vertical = Dimens.SpacingM),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                        }
                }

                items(invoices) { invoice ->
                    InvoiceRow(invoice = invoice, onClick = { onInvoiceClick(invoice) })
                }
            }
        }
    }
}