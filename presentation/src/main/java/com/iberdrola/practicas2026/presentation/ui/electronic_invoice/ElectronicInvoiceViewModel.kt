package com.iberdrola.practicas2026.presentation.ui.electronic_invoice

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ElectronicInvoiceViewModel @Inject constructor() : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _isLegalChecked = MutableStateFlow(false)
    val isLegalChecked: StateFlow<Boolean> = _isLegalChecked.asStateFlow()

    private val _isEmailValid = MutableStateFlow(false)
    val isEmailValid: StateFlow<Boolean> = _isEmailValid.asStateFlow()

    private val _otpCode = MutableStateFlow("")
    val otpCode: StateFlow<String> = _otpCode.asStateFlow()

    // Estados: Idle (0), Loading (1), Success (2)
    private val _otpResendState = MutableStateFlow(0)
    val otpResendState: StateFlow<Int> = _otpResendState.asStateFlow()

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
        _isEmailValid.value = Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()
    }

    fun onLegalCheckedChanged(isChecked: Boolean) {
        _isLegalChecked.value = isChecked
    }

    fun onOtpChanged(newOtp: String) {
        if (newOtp.length <= 6 && newOtp.all { it.isDigit() }) {
            _otpCode.value = newOtp
        }
    }

    fun resendOtp() {
        viewModelScope.launch {
            _otpResendState.value = 1 // Loading
            delay(2000) // Simulamos la petición de red
            _otpResendState.value = 2 // Success
        }
    }

    fun resetState() {
        _email.value = ""
        _isLegalChecked.value = false
        _isEmailValid.value = false
        _otpCode.value = ""
        _otpResendState.value = 0
    }
}