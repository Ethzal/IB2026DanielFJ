package com.iberdrola.practicas2026.presentation.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.presentation.composables.common.ContractListScreen
import com.iberdrola.practicas2026.presentation.composables.common.EmailInputScreen
import com.iberdrola.practicas2026.presentation.composables.common.ModifyEmailInfoScreen
import com.iberdrola.practicas2026.presentation.composables.common.OtpVerificationScreen
import com.iberdrola.practicas2026.presentation.composables.common.SuccessScreen
import com.iberdrola.practicas2026.presentation.ui.electronic_invoice.WizardState
import com.iberdrola.practicas2026.presentation.ui.theme.EnergyAppTheme

@Preview(showBackground = true, name = "Step 1 - Contract List")
@Composable
fun ContractListPreview() {
    EnergyAppTheme {
        ContractListScreen(
            progress = 0.25f,
            onActiveContractClick = {},
            onInactiveContractClick = {},
            onBack = {},
            isGasEnabled = true
        )
    }
}

@Preview(showBackground = true, name = "Step 2A - Info Email")
@Composable
fun ModifyEmailInfoPreview() {
    EnergyAppTheme {
        ModifyEmailInfoScreen(
            onModifyClick = {},
            onBack = {},
            email = "pepe2@a.com"
        )
    }
}

@Preview(showBackground = true, name = "Step 2B - Input Activación")
@Composable
fun EmailInputActivationPreview() {
    EnergyAppTheme {
        val dummyState = WizardState(
            isActivation = true,
            draftEmail = "",
            isLegalChecked = false
        )

        EmailInputScreen(
            state = dummyState,
            progress = 0.5f,
            onEmailChange = {},
            onLegalChange = {},
            onSubmit = {},
            onBack = {},
            onClose = {}
        )
    }
}

@Preview(showBackground = true, name = "Step 2B - Input Modificación")
@Composable
fun EmailInputModifyPreview() {
    EnergyAppTheme {
        val dummyState = WizardState(
            isActivation = false,
            draftEmail = "test@email.com",
            isLegalChecked = true
        )

        EmailInputScreen(
            state = dummyState,
            progress = 0.5f,
            onEmailChange = {},
            onLegalChange = {},
            onSubmit = {},
            onBack = {},
            onClose = {}
        )
    }
}

@Preview(showBackground = true, name = "Step 3 - OTP")
@Composable
fun OtpPreview() {
    EnergyAppTheme {
        val dummyState = WizardState(
            isActivation = true,
            otpCode = "123456",
            otpResendState = 0,
            otpAttemptsLeft = 2,
            hasRequestedResend = true,
            verSoporte = false
        )

        OtpVerificationScreen(
            state = dummyState,
            progress = 0.75f,
            onOtpChange = {},
            onResendClick = {},
            onSubmit = {},
            onBack = {},
            onClose = {},
            onCloseNotice = {}
        )
    }
}

@Preview(showBackground = true, name = "Step 3 - OTP Reenviado")
@Composable
fun OtpResentPreview() {
    EnergyAppTheme {
        val dummyState = WizardState(
            isActivation = true,
            otpCode = "123456",
            otpResendState = 2,
            otpAttemptsLeft = 0,
            hasRequestedResend = true,
            verSoporte = true
        )

        OtpVerificationScreen(
            state = dummyState,
            progress = 0.75f,
            onOtpChange = {},
            onResendClick = {},
            onSubmit = {},
            onBack = {},
            onClose = {},
            onCloseNotice = {}
        )
    }
}

@Preview(showBackground = true, name = "Step 4 - Success")
@Composable
fun SuccessPreview() {
    EnergyAppTheme {
        SuccessScreen(
            isActivation = true,
            onAccept = {},
            displayEmail = "correo@gmail.com"
        )
    }
}