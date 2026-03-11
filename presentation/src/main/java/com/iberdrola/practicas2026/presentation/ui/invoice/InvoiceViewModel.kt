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

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    var usarMocksLocales: Boolean = true

    fun fetchFacturas() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            getInvoicesUseCase(usarMocksLocales)
                .catch { e ->
                    _uiState.value = UiState.Error("Error al cargar: ${e.message}")
                }
                .collect { response ->
                    _uiState.value = UiState.Success(response)
                }
        }
    }

    fun toggleMode(useLocal: Boolean) {
        usarMocksLocales = useLocal
        fetchFacturas()
    }
}