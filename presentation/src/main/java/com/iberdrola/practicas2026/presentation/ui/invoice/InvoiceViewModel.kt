package com.iberdrola.practicas2026.presentation.ui.invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.domain.model.InvoiceResponse
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
import kotlin.collections.emptyList

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val getInvoicesUseCase: GetInvoicesUseCase,
    private val getFeedbackStatus: GetFeedbackStatusUseCase,
    private val updateFeedbackDecision: UpdateFeedbackDecisionUseCase
) : ViewModel() {

    sealed class UiState {
        object Loading : UiState()
        data class Success(val data: InvoiceResponse) : UiState()
        data class Error(val msg: String) : UiState()
    }

    private var originalResponse: InvoiceResponse? = null

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _usarMocksLocales = MutableStateFlow(true)
    val usarMocksLocales: StateFlow<Boolean> = _usarMocksLocales

    init {
        fetchFacturas()
    }

    private val _showFeedbackSheet = MutableStateFlow(false)
    val showFeedbackSheet: StateFlow<Boolean> = _showFeedbackSheet

    // Nuevo estado para saber si mostrar el mensaje de "Gracias"
    private val _showThanksMessage = MutableStateFlow(false)
    val showThanksMessage: StateFlow<Boolean> = _showThanksMessage

    fun onBackClicked(onConfirmExit: () -> Unit) {
        viewModelScope.launch {
            // 1. Siempre incrementamos el contador de "intento de salida"
            updateFeedbackDecision.incrementExit()

            // 2. Comprobamos si toca preguntar según la lógica (10, 3 o 0)
            getFeedbackStatus().first().let { shouldShow ->
                if (shouldShow) {
                    _showFeedbackSheet.value = true
                } else {
                    onConfirmExit()
                }
            }
        }
    }

    // Caso A: El usuario pulsa una carita (Valoración)
    fun onRatingSelected(rating: Int) {
        viewModelScope.launch {
            // Guardamos el retraso de 10
            updateFeedbackDecision.setDelay(isRated = true)
            // Mostramos el mensaje de agradecimiento en el mismo BottomSheet
            _showThanksMessage.value = true
        }
    }

    // Caso B: El usuario pulsa "Responder más tarde"
    fun onLaterClicked(onConfirmExit: () -> Unit) {
        viewModelScope.launch {
            // Guardamos el retraso de 3
            updateFeedbackDecision.setDelay(isRated = false)
            _showFeedbackSheet.value = false
            onConfirmExit()
        }
    }

    // Caso C: El usuario cierra el sheet
    fun onSheetDismissed(onConfirmExit: () -> Unit) {
        _showFeedbackSheet.value = false
        _showThanksMessage.value = false
        onConfirmExit()
    }

    fun fetchFacturas() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            getInvoicesUseCase(_usarMocksLocales.value)
                .catch { e -> _uiState.value = UiState.Error(e.message ?: "Error") }
                .collect { response ->
                    originalResponse = response
                    filterInvoicesByType("Factura Luz")
                }
        }
    }

    fun filterInvoicesByType(type: String) {
        val response = originalResponse ?: return

        // Filtramos la lista bruta que guardamos en allInvoices
        val filtered = response.allInvoices.filter { it.type == type }

        val last = filtered.firstOrNull()
        val history = if (filtered.size > 1) filtered.drop(1) else emptyList()

        // IMPORTANTE: Pasamos un objeto InvoiceResponse al Success
        _uiState.value = UiState.Success(
            data = InvoiceResponse(
                lastInvoice = last,
                history = history,
                allInvoices = response.allInvoices // Mantenemos la original
            )
        )
    }

    fun toggleMode(useLocal: Boolean) {
        _usarMocksLocales.value = useLocal
        fetchFacturas()
    }
}