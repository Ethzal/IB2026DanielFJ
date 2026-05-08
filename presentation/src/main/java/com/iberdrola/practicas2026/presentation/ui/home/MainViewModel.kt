package com.iberdrola.practicas2026.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.iberdrola.practicas2026.domain.repository.SettingsRepository
import com.iberdrola.practicas2026.presentation.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    private val _isInvoiceLoading = MutableStateFlow(false)
    val isInvoiceLoading = _isInvoiceLoading.asStateFlow()

    private var loadingJob: kotlinx.coroutines.Job? = null

    val isLocalMode = settingsRepository.isLocalMode().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), true
    )

    fun toggleMode(enabled: Boolean) {
        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {
            settingsRepository.toggleLocalMode(enabled)

            _isInvoiceLoading.value = true

            val msg = if (enabled) R.string.modo_local_cargando_mocks else R.string.modo_remoto_conectando_a_api
            _uiEvent.emit(msg)

            delay(3000)

            _isInvoiceLoading.value = false
        }
    }
}