package com.iberdrola.practicas2026.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.domain.model.Invoice
import com.iberdrola.practicas2026.domain.repository.SettingsRepository
import com.iberdrola.practicas2026.domain.usecase.GetInvoicesUseCase
import com.iberdrola.practicas2026.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

sealed class InvoiceState {
    object Loading : InvoiceState()
    data class Success(val invoice: Invoice?) : InvoiceState()
    object Error : InvoiceState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val getInvoicesUseCase: GetInvoicesUseCase
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    val isLocalMode = settingsRepository.isLocalMode().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), true
    )

    private val _lastInvoiceState = MutableStateFlow<InvoiceState>(InvoiceState.Loading)
    val lastInvoiceState: StateFlow<InvoiceState> = _lastInvoiceState.asStateFlow()

    private var fetchJob: Job? = null

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsRepository.isLocalMode().collect { isLocal ->
                fetchLastInvoice(isLocal)
            }
        }
    }

    private fun fetchLastInvoice(isLocal: Boolean) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _lastInvoiceState.value = InvoiceState.Loading

            delay(3000)

            getInvoicesUseCase(isLocal).collect { response ->
                if (response.allInvoices.isEmpty()) {
                    _lastInvoiceState.value = InvoiceState.Error
                } else {
                    val latestInvoice = response.allInvoices.maxByOrNull { invoice ->
                        try {
                            LocalDate.parse(invoice.date, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        } catch (_: Exception) {
                            LocalDate.MIN
                        }
                    }
                    _lastInvoiceState.value = InvoiceState.Success(latestInvoice)
                }
            }
        }
    }

    fun toggleMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.toggleLocalMode(enabled)
            val msg = if (enabled) {
                R.string.modo_local_cargando_mocks
            } else {
                R.string.modo_remoto_conectando_a_api
            }
            _uiEvent.emit(msg)
        }
    }
}