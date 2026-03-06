package com.iberdrola.practicas2026.DanielFJ.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.DanielFJ.data.InvoiceDetail
import com.iberdrola.practicas2026.DanielFJ.data.InvoiceItem
import com.iberdrola.practicas2026.DanielFJ.data.InvoiceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor() : ViewModel() {

    // Manejo de estados de la UI
    sealed class UiState {
        object Loading : UiState()
        data class Success(val data: InvoiceResponse) : UiState()
        data class Error(val msg: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    // true = Mocks locales, false = Llamada remota
    var usarMocksLocales: Boolean = true

    fun fetchFacturas() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                if (usarMocksLocales) {
                    // Delay aleatorio entre 1 y 3 segundos
                    val tiempoEspera = (1000..3000).random().toLong()
                    delay(tiempoEspera)
                    _uiState.value = UiState.Success(getLocalMock())
                } else {
                    delay(500)
                    _uiState.value = UiState.Success(getLocalMock())
                }
            } catch (_: Exception) {
                _uiState.value = UiState.Error("Error al cargar facturas")
            }
        }
    }

    private fun getLocalMock() = InvoiceResponse(
        lastInvoice = InvoiceDetail(
            "F1",
            "Factura Luz",
            20.00,
            "01 feb. 2024",
            "04 mar. 2024",
            "Pendiente de Pago"
        ),
        history = listOf(
            InvoiceItem("F2", "8 de marzo", "Factura Luz", 20.00, "Pendiente de Pago"),
            InvoiceItem("F3", "6 de febrero", "Factura Luz", 20.10, "Pendiente de Pago"),
            InvoiceItem("F4", "6 de noviembre", "Factura Luz", 150.43, "Pagada")
        )
    )
}