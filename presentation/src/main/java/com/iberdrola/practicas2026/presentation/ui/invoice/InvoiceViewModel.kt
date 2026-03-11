package com.iberdrola.practicas2026.presentation.ui.invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.domain.model.InvoiceResponse
import com.iberdrola.practicas2026.domain.usecase.GetInvoicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val getInvoicesUseCase: GetInvoicesUseCase
) : ViewModel() {

    sealed class UiState {
        object Loading : UiState()
        data class Success(val data: InvoiceResponse) : UiState()
        data class Error(val msg: String) : UiState()
    }

    private var originalResponse: InvoiceResponse? = null

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    var usarMocksLocales: Boolean = true

    fun fetchFacturas() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            getInvoicesUseCase(usarMocksLocales)
                .catch { e -> _uiState.value = UiState.Error(e.message ?: "Error") }
                .collect { response ->
                    originalResponse = response
                    filterInvoicesByType("Factura Luz")
                }
        }
    }

    fun filterInvoicesByType(type: String) {
        val data = originalResponse ?: return

        val filteredHistory = data.history.filter { it.type == type }

        val filteredLastInvoice = if (data.lastInvoice.type == type) {
            data.lastInvoice
        } else {
            null
        }

        _uiState.value = UiState.Success(
            data.copy(history = filteredHistory)
        )
    }

    fun toggleMode(useLocal: Boolean) {
        usarMocksLocales = useLocal
        fetchFacturas()
    }
}