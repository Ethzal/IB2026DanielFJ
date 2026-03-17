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
import kotlin.collections.emptyList

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

    private val _usarMocksLocales = MutableStateFlow(true)
    val usarMocksLocales: StateFlow<Boolean> = _usarMocksLocales

    init {
        fetchFacturas()
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