package com.iberdrola.practicas2026.presentation.ui.electronic_invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.core.utils.isEmailValid
import com.iberdrola.practicas2026.domain.repository.OtpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class WizardStep {
    CONTRACT_LIST, MODIFY_INFO, EMAIL_INPUT, OTP, SUCCESS
}

// 1. ESTADO: Representa toda la información necesaria para pintar la UI
data class WizardState(
    val step: WizardStep = WizardStep.CONTRACT_LIST,
    val isActivation: Boolean = true,

    // Datos de contrato
    val lightContractEmail: String = "facturaluz@gmail.com",

    // Formulario Email
    val draftEmail: String = "",
    val isLegalChecked: Boolean = false,

    // Formulario OTP
    val otpCode: String = "",
    val otpResendState: Int = 0, // 0: Idle, 1: Loading, 2: Sent, 3: Limit
    val otpAttemptsLeft: Int = 3,
    val hasRequestedResend: Boolean = false,
    val verSoporte: Boolean = false
) {
    val isLoading get() = otpResendState == 1
    val isEmailValid get() = isEmailValid(draftEmail)
    val isOtpValid get() = otpCode.length == 6
}

// 2. EVENTOS: Intenciones del usuario que la UI manda al ViewModel
sealed class WizardEvent {
    object SelectActiveContract : WizardEvent()
    object SelectInactiveContract : WizardEvent()
    object GoToEmailInput : WizardEvent()

    data class UpdateEmail(val email: String) : WizardEvent()
    data class UpdateLegal(val checked: Boolean) : WizardEvent()
    object SubmitEmail : WizardEvent()

    data class UpdateOtp(val code: String) : WizardEvent()
    object SubmitOtp : WizardEvent()
    object ResendOtp : WizardEvent()
    object DismissNotice : WizardEvent()

    object AcceptSuccess : WizardEvent()
    object NavigateBack : WizardEvent()
    object CloseWizard : WizardEvent()
}

// 3. EFECTOS: Acciones de un solo uso (One-shot events) hacia la UI
sealed class WizardEffect {
    object ExitWizard : WizardEffect()
}

@HiltViewModel
class ElectronicInvoiceViewModel @Inject constructor(
    private val otpRepository: OtpRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WizardState())
    val state: StateFlow<WizardState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<WizardEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            otpRepository.otpAttempts.collect { (savedAttempts, lastTime) ->
                val currentTime = System.currentTimeMillis()
                val dayInMillis = 24 * 60 * 60 * 1000L

                if (currentTime - lastTime >= dayInMillis) {
                    otpRepository.saveOtpAttempts(3, 0L)
                    _state.update { it.copy(otpAttemptsLeft = 3, verSoporte = false) }
                } else {
                    _state.update { it.copy(otpAttemptsLeft = savedAttempts) }
                }
            }
        }
    }

    fun resetState() {
        _state.value = WizardState()
    }

    fun onEvent(event: WizardEvent) {
        when (event) {
            is WizardEvent.SelectActiveContract -> {
                _state.update {
                    it.copy(
                        isActivation = false,
                        draftEmail = it.lightContractEmail,
                        step = WizardStep.MODIFY_INFO
                    )
                }
            }
            is WizardEvent.SelectInactiveContract -> {
                _state.update {
                    it.copy(
                        isActivation = true,
                        draftEmail = "",
                        step = WizardStep.EMAIL_INPUT
                    )
                }
            }
            is WizardEvent.GoToEmailInput -> {
                _state.update { it.copy(step = WizardStep.EMAIL_INPUT) }
            }
            is WizardEvent.UpdateEmail -> {
                _state.update { it.copy(draftEmail = event.email) }
            }
            is WizardEvent.UpdateLegal -> {
                _state.update { it.copy(isLegalChecked = event.checked) }
            }
            is WizardEvent.SubmitEmail -> handleSubmitEmail()
            is WizardEvent.UpdateOtp -> {
                if (event.code.length <= 6 && event.code.all { it.isDigit() }) {
                    _state.update { it.copy(otpCode = event.code) }
                }
            }
            is WizardEvent.SubmitOtp -> {
                _state.update { it.copy(step = WizardStep.SUCCESS) }
            }
            is WizardEvent.ResendOtp -> handleResendOtp()
            is WizardEvent.DismissNotice -> {
                _state.update { it.copy(otpResendState = 0) }
            }
            is WizardEvent.AcceptSuccess -> handleSuccessAccept()
            is WizardEvent.NavigateBack -> handleBack()
            is WizardEvent.CloseWizard -> handleClose()
        }
    }

    private fun handleResendOtp() {
        val currentState = _state.value
        if (currentState.otpAttemptsLeft > 0) {
            val newAttempts = currentState.otpAttemptsLeft - 1
            val currentTime = if (newAttempts == 0) System.currentTimeMillis() else 0L
            viewModelScope.launch { otpRepository.saveOtpAttempts(newAttempts, currentTime) }
            _state.update {
                it.copy(
                    otpAttemptsLeft = it.otpAttemptsLeft - 1,
                    hasRequestedResend = true,
                    otpResendState = 1 // Loading
                )
            }
            viewModelScope.launch {
                delay(2000) // Simulación de API
                _state.update { it.copy(otpResendState = 2) } // Sent
            }
        } else {
            _state.update { it.copy(verSoporte = true, otpResendState = 3) } // Limit Reached
        }
    }

    private fun handleSuccessAccept() {
        val currentState = _state.value

        // Limpiamos los estados de formularios
        val cleanState = currentState.copy(
            otpCode = "",
            isLegalChecked = false,
            hasRequestedResend = false,
            verSoporte = false,
            otpResendState = 0
        )

        if (!currentState.isActivation) {
            // Modificación exitosa del contrato activo
            _state.value = cleanState.copy(
                lightContractEmail = currentState.draftEmail,
                step = WizardStep.MODIFY_INFO
            )
        } else {
            // Activación exitosa del inactivo
            _state.value = cleanState.copy(step = WizardStep.CONTRACT_LIST)
        }
    }

    private fun handleSubmitEmail() {
        val currentState = _state.value
        if (!currentState.isActivation &&
            currentState.draftEmail.trim().equals(currentState.lightContractEmail.trim(), ignoreCase = true)) {
            return
        }
        _state.update { it.copy(step = WizardStep.OTP, otpCode = "") }
    }

    private fun handleBack() {
        val s = _state.value
        when (s.step) {
            WizardStep.OTP -> _state.update { it.copy(step = WizardStep.EMAIL_INPUT) }
            WizardStep.EMAIL_INPUT -> {
                if (!s.isActivation) _state.update { it.copy(step = WizardStep.MODIFY_INFO) }
                else _state.update { it.copy(step = WizardStep.CONTRACT_LIST) }
            }
            WizardStep.MODIFY_INFO -> _state.update { it.copy(step = WizardStep.CONTRACT_LIST) }
            WizardStep.CONTRACT_LIST -> exit()
            WizardStep.SUCCESS -> { /* No se puede volver atrás desde éxito */ }
        }
    }

    private fun handleClose() {
        val s = _state.value
        if (!s.isActivation) {
            _state.update { it.copy(step = WizardStep.MODIFY_INFO) }
        } else {
            _state.update { it.copy(step = WizardStep.CONTRACT_LIST) }
        }
    }

    private fun exit() {
        viewModelScope.launch { _effect.emit(WizardEffect.ExitWizard) }
    }
}