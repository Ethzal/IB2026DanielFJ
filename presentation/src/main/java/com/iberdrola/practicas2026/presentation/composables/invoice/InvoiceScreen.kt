package com.iberdrola.practicas2026.presentation.composables.invoice

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iberdrola.practicas2026.domain.model.Invoice
import com.iberdrola.practicas2026.domain.model.InvoiceResponse
import com.iberdrola.practicas2026.domain.model.InvoiceType
import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.presentation.composables.common.FeedbackBottomSheet
import com.iberdrola.practicas2026.presentation.composables.common.InvoiceRow
import com.iberdrola.practicas2026.presentation.composables.common.ShimmerItem
import com.iberdrola.practicas2026.presentation.ui.invoice.InvoiceViewModel
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens
import com.iberdrola.practicas2026.presentation.ui.theme.TextSecondary
import com.iberdrola.practicas2026.presentation.ui.theme.TextMain
import kotlinx.coroutines.launch

@Composable
fun InvoiceScreen(
    viewModel: InvoiceViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
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

    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
        val type = if (pagerState.currentPage == 0) InvoiceType.LIGHT else InvoiceType.GAS
        viewModel.filterInvoices(type)
    }

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
            title = { Text(stringResource(R.string.informacion)) },
            text = { Text(stringResource(R.string.factura_no_disponible)) },
            confirmButton = {
                TextButton(onClick = { showNotAvailableDialog = false }) {
                    Text(stringResource(R.string.aceptar))
                }
            }
        )
    }

    if (showFilterScreen) {
        FilterScreen(
            currentFilter = invoiceFilter,
            amountBounds = amountBounds,
            onApplyFilters = { viewModel.applyFilters(it) },
            onClearFilters = { viewModel.clearFilters() },
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
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = Color.White,
                    contentColor = BrandGreen,
                    edgePadding = Dimens.SpacingM,
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
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = {
                                Text(
                                    title,
                                    color = if (selectedTabIndex == index) TextMain else TextSecondary
                                )
                            }
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    userScrollEnabled = true,
                    beyondViewportPageCount = 1
                ) { pageIndex ->

                    val type = if (pageIndex == 0) InvoiceType.LIGHT else InvoiceType.GAS

                    // Obtenemos el estado específico de esta página
                    val pageUiState = viewModel.uiStates.collectAsStateWithLifecycle().value[type] ?: InvoiceViewModel.UiState.Loading

                    when (pageUiState) {
                        is InvoiceViewModel.UiState.Loading -> {
                            Column { repeat(3) { ShimmerItem() } }
                        }
                        is InvoiceViewModel.UiState.Success -> {
                            InvoiceList(
                                data = pageUiState.data,
                                onInvoiceClick = { showNotAvailableDialog = true },
                                onFilterClick = { showFilterScreen = true },
                                isFiltering = isFiltering
                            )
                        }

                        is InvoiceViewModel.UiState.Error -> {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(text = pageUiState.msg, color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InvoiceList(data: InvoiceResponse, onInvoiceClick: (Invoice) -> Unit, onFilterClick: () -> Unit, isFiltering: Boolean) {
    val groupedHistory = data.history.groupBy {
        it.date.take(4)
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Dimens.SpacingM)
    ) {
        // Tarjeta principal
        item {
            data.lastInvoice?.let { last ->
                LastInvoiceCard(
                    invoice = last,
                    onClick = { onInvoiceClick(last) }
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.SpacingM, bottom = Dimens.SpacingM),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.historico_de_facturas), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                FilterButton(onClick = onFilterClick, isFilterActive = isFiltering)
            }
        }

        groupedHistory.forEach { (year, invoices) ->
            item {
                Text(
                    text = year,
                    modifier = Modifier.padding(vertical = Dimens.SpacingS),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(invoices) { invoice ->
                InvoiceRow(invoice = invoice, onClick = { onInvoiceClick(invoice) })
            }
        }
    }
}