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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _showFeedbackSheet = MutableStateFlow(false)
    val showFeedbackSheet: StateFlow<Boolean> = _showFeedbackSheet

    private val _showThanksMessage = MutableStateFlow(false)
    val showThanksMessage: StateFlow<Boolean> = _showThanksMessage

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
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            getInvoicesUseCase(isLocal)
                .catch { e -> _uiState.value = UiState.Error(e.message ?: "Error desconocido") }
                .collect { response ->
                    allInvoicesCached = response.allInvoices
                    filterInvoices(InvoiceType.LIGHT) // Carga inicial por defecto
                }
        }
    }

    /**
     * Filtra las facturas usando el UseCase de dominio.
     * No necesitamos volver a llamar a la API, usamos la cache.
     */
    fun filterInvoices(type: InvoiceType) {
        val filteredResponse = filterInvoicesUseCase(allInvoicesCached, type)
        _uiState.value = UiState.Success(filteredResponse)
    }

    // FEEDBACK
    fun onBackClicked(onConfirmExit: () -> Unit) {
        viewModelScope.launch {
            // 1. Siempre incrementamos el contador de intentos de salida
            updateFeedbackDecision.incrementExit()

            // 2. Consultamos si toca mostrar el diálogo (lógica 10, 3, 0)
            getFeedbackStatus().first().let { shouldShow ->
                if (shouldShow) {
                    _showFeedbackSheet.value = true
                } else {
                    onConfirmExit()
                }
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
}