package com.iberdrola.practicas2026.presentation.composables.invoice

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
    val isGasEnabled by viewModel.isGasEnabled.collectAsStateWithLifecycle()

    val tabs = if (isGasEnabled) {
        listOf(stringResource(R.string.luz), stringResource(R.string.gas))
    } else {
        listOf(stringResource(R.string.luz))
    }
    val snackbarHostState = remember { SnackbarHostState() }

    val showFeedback by viewModel.showFeedbackSheet.collectAsStateWithLifecycle()
    val showThanks by viewModel.showThanksMessage.collectAsStateWithLifecycle()
    var showNotAvailableDialog by remember { mutableStateOf(false) }

    // FILTROS
    val invoiceFilter by viewModel.invoiceFilter.collectAsStateWithLifecycle()
    val amountBounds by viewModel.amountBounds.collectAsStateWithLifecycle()
    var showFilterScreen by rememberSaveable { mutableStateOf(false) }
    val isFiltering by viewModel.isFiltering.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    var isExiting by remember { mutableStateOf(false) }


    val handleBack = {
        if (!isExiting) {
            isExiting = true
            showNotAvailableDialog = false
            viewModel.onBackClicked(onConfirmExit = onBackClick)
        }
    }

    BackHandler {
        if (showFilterScreen) {
            showFilterScreen = false
        } else {
            handleBack()
        }
    }

    LaunchedEffect(showFeedback) {
        if (!showFeedback && !showThanks) {
            isExiting = false
        }
    }

    // EVENTOS
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

    LaunchedEffect(tabs.size) {
        if (pagerState.currentPage >= tabs.size) {
            pagerState.scrollToPage(0)
        }
    }

    fun showSnackbar(message: String) {
        coroutineScope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
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

    val minDateAllowed by viewModel.minDateAllowed.collectAsStateWithLifecycle()

    if (showFilterScreen) {
        FilterScreen(
            currentFilter = invoiceFilter,
            amountBounds = amountBounds,
            minDateAllowed = minDateAllowed,
            onFilterStateChanged = { range, statuses ->
                viewModel.updateDynamicConstraints(range, statuses)
            },
            onApplyFilters = {
                viewModel.applyFilters(it)
                val count = viewModel.getResultCount()
                showFilterScreen = false
                val message = context.resources.getQuantityString(R.plurals.filtros_aplicados, count, count)
                showSnackbar(message)
            },
            onClearFilters = { viewModel.clearFilters() },
            onBack = { showFilterScreen = false }
        )
    } else {
        Scaffold(
            topBar = {
                InvoiceHeader(
                    onBack = { handleBack() }
                )
            },
            containerColor = Color.White,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = DarkGray.copy(alpha = 0.9f),
                        contentColor = White
                    )
                }
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                SlidingTabsSection(pagerState, tabs, coroutineScope)

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    beyondViewportPageCount = 1
                ) { pageIndex ->
                    val type = if (pageIndex == 0) InvoiceType.LIGHT else InvoiceType.GAS
                    val uiStatesMap by viewModel.uiStates.collectAsStateWithLifecycle()
                    val pageUiState = uiStatesMap[type] ?: InvoiceViewModel.UiState.Loading

                    LaunchedEffect(type) { viewModel.filterInvoices(type) }

                    when (pageUiState) {
                        is InvoiceViewModel.UiState.Loading -> {
                            val brush = rememberShimmerBrush()
                            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(Dimens.SpacingM)) {
                                // Tarjeta principal destacada
                                item { ShimmerLastInvoiceCard(brush) }

                                // Título Histórico + Botón Filtrar
                                item {
                                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.SpacingM), horizontalArrangement = Arrangement.SpaceBetween) {
                                        // Caja para "Histórico de facturas"
                                        Box(modifier = Modifier.width(Dimens.ShimmerCardHeight).height(24.dp).background(brush))

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
                                onInvoiceClick = { invoice ->
                                    viewModel.logButtonClick("btn_factura_${invoice.id}")
                                    if (!isExiting && !showFeedback) {
                                        showNotAvailableDialog = true
                                    }
                                },
                                onFilterClick = {
                                    viewModel.logButtonClick("btn_abrir_filtros")
                                    if (!isExiting && !showFeedback) showFilterScreen = true
                                },
                                isFiltering = isFiltering,
                                onClearFilters = { viewModel.clearFilters() }
                            )
                        }

                        is InvoiceViewModel.UiState.Error -> {
                            ErrorStateView(
                                message = pageUiState.msg,
                                onRetry = { viewModel.fetchFacturas(isLocal = false) }
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

    val hasHistoryResults = data.history.isNotEmpty()
    val listState = rememberLazyListState()

    // Scroll automático al histórico cuando se aplican filtros
    LaunchedEffect(isFiltering) {
        if (isFiltering) {
            listState.animateScrollToItem(1)
        } else {
            listState.animateScrollToItem(0)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = Dimens.SpacingXL) // Espacio al final
    ) {
        // ÍNDICE 0: TARJETA PERMANENTE
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.SpacingM, vertical = Dimens.SpacingM)
            ) {
                data.lastInvoice?.let { last ->
                    LastInvoiceCard(
                        invoice = last,
                        onClick = { onInvoiceClick(last) }
                    )
                }
            }
        }

        // ÍNDICE 1: ENCABEZADO + BOTÓN
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Dimens.SpacingM, end = Dimens.SpacingM, bottom = Dimens.SpacingM),
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

        if (!hasHistoryResults) {
            item {
                Box(
                    modifier = Modifier
                        .fillParentMaxHeight()
                        .padding(bottom = Dimens.SpacingXL),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyStateView(
                        iconRes = R.drawable.ic_energy_empty,
                        title = stringResource(R.string.sin_facturas),
                        message = stringResource(R.string.no_hemos_encontrado_facturas),
                        onClearFilters = onClearFilters
                    )
                }
            }
        } else {
            groupedHistory.forEach { (year, invoices) ->
                item {
                    Text(
                        text = year,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.SpacingM, vertical = Dimens.SpacingM),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(invoices) { invoice ->
                    InvoiceRow(invoice = invoice, onClick = { onInvoiceClick(invoice) })
                }
            }

            if (isFiltering) {
                item {
                    Spacer(modifier = Modifier.fillParentMaxHeight())
                }
            }
        }
    }
}