package com.iberdrola.practicas2026.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.iberdrola.practicas2026.domain.repository.SettingsRepository
import com.iberdrola.practicas2026.domain.usecase.GetInvoicesUseCase
import com.iberdrola.practicas2026.domain.model.Invoice
import com.iberdrola.practicas2026.presentation.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val getInvoicesUseCase: GetInvoicesUseCase
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    private val _isInvoiceLoading = MutableStateFlow(false)
    val isInvoiceLoading = _isInvoiceLoading.asStateFlow()

    private val _lastInvoice = MutableStateFlow<Invoice?>(null)
    val lastInvoice = _lastInvoice.asStateFlow()

    private val _hasError = MutableStateFlow(false)
    val hasError = _hasError.asStateFlow()

    private var loadingJob: kotlinx.coroutines.Job? = null

    val isLocalMode = settingsRepository.isLocalMode().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), true
    )

    init {
        viewModelScope.launch {
            isLocalMode.collect { isLocal ->
                loadInvoices(isLocal)
            }
        }
    }

    fun toggleMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.toggleLocalMode(enabled)
            val msg = if (enabled) R.string.modo_local_cargando_mocks else R.string.modo_remoto_conectando_a_api
            _uiEvent.emit(msg)
        }
    }

    private fun loadInvoices(isLocal: Boolean) {
        loadingJob?.cancel()
        loadingJob = viewModelScope.launch {
            _isInvoiceLoading.value = true
            _hasError.value = false

            try {
                getInvoicesUseCase(isLocal)
                    .catch {
                        _hasError.value = true
                        _isInvoiceLoading.value = false
                    }
                    .collect { response ->
                        val last = response.allInvoices.maxByOrNull { it.date }
                        _lastInvoice.value = last
                        _hasError.value = false
                        _isInvoiceLoading.value = false
                    }
            } catch (_: Exception) {
                _hasError.value = true
                _isInvoiceLoading.value = false
            }
        }
    }
}