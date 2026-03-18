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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<Int>()
    val uiEvent = _uiEvent.asSharedFlow()

    val isLocalMode = settingsRepository.isLocalMode().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), true
    )

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