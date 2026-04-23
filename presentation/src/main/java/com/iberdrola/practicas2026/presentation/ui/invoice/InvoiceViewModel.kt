package com.iberdrola.practicas2026.presentation.ui.invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.domain.model.Invoice
import com.iberdrola.practicas2026.domain.model.InvoiceResponse
import com.iberdrola.practicas2026.domain.model.InvoiceType
import com.iberdrola.practicas2026.domain.repository.SettingsRepository
import com.iberdrola.practicas2026.domain.usecase.FilterInvoicesUseCase
import com.iberdrola.practicas2026.domain.usecase.GetFeedbackStatusUseCase
import com.iberdrola.practicas2026.domain.usecase.GetInvoicesUseCase
import com.iberdrola.practicas2026.domain.usecase.UpdateFeedbackDecisionUseCase
import com.iberdrola.practicas2026.domain.model.InvoiceFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class InvoiceEvent {
    data class SwitchToTab(val index: Int) : InvoiceEvent()
}

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val getInvoicesUseCase: GetInvoicesUseCase,
    private val filterInvoicesUseCase: FilterInvoicesUseCase,
    private val settingsRepository: SettingsRepository,
    private val getFeedbackStatus: GetFeedbackStatusUseCase,
    private val updateFeedbackDecision: UpdateFeedbackDecisionUseCase
) : ViewModel() {

    sealed class UiState {
        object Loading : UiState()
        data class Success(val data: InvoiceResponse) : UiState()
        data class Error(val msg: String) : UiState()
    }

    // Cache local para evitar llamadas a red al cambiar de Tab
    private var allInvoicesCached: List<Invoice> = emptyList()

    private val _uiStates = MutableStateFlow<Map<InvoiceType, UiState>>(emptyMap())
    val uiStates: StateFlow<Map<InvoiceType, UiState>> = _uiStates

    private val _showFeedbackSheet = MutableStateFlow(false)
    val showFeedbackSheet: StateFlow<Boolean> = _showFeedbackSheet

    private val _showThanksMessage = MutableStateFlow(false)
    val showThanksMessage: StateFlow<Boolean> = _showThanksMessage

    // FILTROS
    private val _invoiceFilter = MutableStateFlow(InvoiceFilter())
    val invoiceFilter: StateFlow<InvoiceFilter> = _invoiceFilter

    // Canal de eventos para la UI
    private val _events = MutableSharedFlow<InvoiceEvent>()
    val events = _events.asSharedFlow()

    private var currentTabIndex = 0

    fun updateCurrentTab(index: Int) {
        currentTabIndex = index
    }

    private var fetchJob: Job? = null

    val isFiltering: StateFlow<Boolean> = _invoiceFilter.map { filter ->
        filter.dateFrom != null ||
                filter.dateTo != null ||
                filter.amountRange != null ||
                filter.statuses.isNotEmpty()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    private val _amountBounds = MutableStateFlow(0f..100f) // Límites dinámicos
    val amountBounds: StateFlow<ClosedFloatingPointRange<Float>> = _amountBounds

    private var currentInvoiceType = InvoiceType.LIGHT

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsRepository.isLocalMode().collect { isLocal ->
                fetchFacturas(isLocal)
            }
        }
    }

    fun fetchFacturas(isLocal: Boolean) {
        fetchJob?.cancel() // Cancela la carga anterior si está en curso
        fetchJob = viewModelScope.launch {
            _uiStates.value = mapOf(
                InvoiceType.LIGHT to UiState.Loading,
                InvoiceType.GAS to UiState.Loading
            )

            getInvoicesUseCase(isLocal)
                .catch { e ->
                    // Si hay error, lo ponemos en ambos para que el usuario se entere
                    _uiStates.update { currentMap ->
                        currentMap + (InvoiceType.LIGHT to UiState.Error(e.message ?: "Sin conexión")) +
                                (InvoiceType.GAS to UiState.Error(e.message ?: "Sin conexión"))
                    }
                }
                .collect { response ->
                    allInvoicesCached = response.allInvoices

                    // Cálculo dinámico para el slider
                    if (allInvoicesCached.isNotEmpty()) {
                        val min = allInvoicesCached.minOf { it.amount.toFloat() }
                        val max = allInvoicesCached.maxOf { it.amount.toFloat() }
                        _amountBounds.value = if (min == max) 0f..(max + 1f) else min..max
                    }
                    filterInvoices(InvoiceType.LIGHT)
                    filterInvoices(InvoiceType.GAS)
                }
        }
    }

    /**
     * Filtra las facturas usando el UseCase de dominio.
     * No necesitamos volver a llamar a la API, usamos la cache.
     */
    fun filterInvoices(type: InvoiceType) {
        currentInvoiceType = type

        if (allInvoicesCached.isEmpty()) {
            _uiStates.update { it + (type to UiState.Loading) }
            return
        }

        // SI HAY DATOS (Caché), calculamos el éxito
        val filteredResponse = filterInvoicesUseCase(allInvoicesCached, type, _invoiceFilter.value)
        _uiStates.update { it + (type to UiState.Success(filteredResponse)) }
    }

    fun applyFilters(newFilter: InvoiceFilter) {
        _invoiceFilter.value = newFilter

        // 2. Calculamos los resultados de AMBAS pestañas inmediatamente
        val luzRes = filterInvoicesUseCase(allInvoicesCached, InvoiceType.LIGHT, newFilter)
        val gasRes = filterInvoicesUseCase(allInvoicesCached, InvoiceType.GAS, newFilter)

        // Actualizamos los estados de la UI
        _uiStates.update { it + (InvoiceType.LIGHT to UiState.Success(luzRes)) }
        _uiStates.update { it + (InvoiceType.GAS to UiState.Success(gasRes)) }

        // 3. LÓGICA DE REDIRECCIÓN INTELIGENTE
        viewModelScope.launch {
            val hasLuz = (luzRes.lastInvoice != null || luzRes.history.isNotEmpty())
            val hasGas = (gasRes.lastInvoice != null || gasRes.history.isNotEmpty())

            if (currentTabIndex == 0 && !hasLuz && hasGas) {
                // Estoy en LUZ, no hay nada, pero en GAS sí -> Ir a GAS (index 1)
                _events.emit(InvoiceEvent.SwitchToTab(1))
            } else if (currentTabIndex == 1 && !hasGas && hasLuz) {
                // Estoy en GAS, no hay nada, pero en LUZ sí -> Ir a LUZ (index 0)
                _events.emit(InvoiceEvent.SwitchToTab(0))
            }
        }
    }

    fun clearFilters() {
        _invoiceFilter.value = InvoiceFilter()
        filterInvoices(InvoiceType.LIGHT)
        filterInvoices(InvoiceType.GAS)
    }

    // FEEDBACK
    fun onBackClicked(onConfirmExit: () -> Unit) {
        viewModelScope.launch {
            // 1. Siempre incrementamos el contador de intentos de salida
            updateFeedbackDecision.incrementExit()

            // 2. Consultamos si toca mostrar el diálogo (lógica 10, 3, 0)
            val shouldShow = getFeedbackStatus().first()
            if (shouldShow) {
                _showFeedbackSheet.value = true
            } else {
                onConfirmExit()
            }
        }
    }

    fun onRatingSelected(rating: Int) {
        viewModelScope.launch {
            updateFeedbackDecision.setDelay(isRated = true) // Próximo aviso en 10
            _showThanksMessage.value = true
        }
    }

    fun onLaterClicked(onConfirmExit: () -> Unit) {
        viewModelScope.launch {
            updateFeedbackDecision.setDelay(isRated = false) // Próximo aviso en 3
            _showFeedbackSheet.value = false
            onConfirmExit()
        }
    }

    fun onSheetDismissed(onConfirmExit: () -> Unit) {
        _showFeedbackSheet.value = false
        _showThanksMessage.value = false
        onConfirmExit()
    }

    fun getResultCount(): Int {
        val luz = filterInvoicesUseCase(allInvoicesCached, InvoiceType.LIGHT, _invoiceFilter.value)
        val gas = filterInvoicesUseCase(allInvoicesCached, InvoiceType.GAS, _invoiceFilter.value)
        val totalLuz = luz.history.size + (if (luz.lastInvoice != null) 1 else 0)
        val totalGas = gas.history.size + (if (gas.lastInvoice != null) 1 else 0)

        return totalLuz + totalGas
    }
}