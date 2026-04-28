package com.iberdrola.practicas2026.presentation.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.presentation.composables.common.ContractListScreen
import com.iberdrola.practicas2026.presentation.composables.common.EmailInputScreen
import com.iberdrola.practicas2026.presentation.composables.common.ModifyEmailInfoScreen
import com.iberdrola.practicas2026.presentation.composables.common.OtpVerificationScreen
import com.iberdrola.practicas2026.presentation.composables.common.SuccessScreen
import com.iberdrola.practicas2026.presentation.ui.theme.EnergyAppTheme

@Preview(showBackground = true, name = "Step 1 - Contract List")
@Composable
fun ContractListPreview() {
    EnergyAppTheme {
        ContractListScreen(
            progress = 0.25f,
            onActiveContractClick = {},
            onInactiveContractClick = {},
            onBack = {}
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
        EmailInputScreen(
            isActivation = true,
            email = "",
            isEmailValid = false,
            isLegalChecked = false,
            onEmailChange = {},
            onLegalChange = {},
            progress = 0.5f,
            onNext = {},
            onBack = {},
            onClose = {}
        )
    }
}

@Preview(showBackground = true, name = "Step 2B - Input Modificación")
@Composable
fun EmailInputModifyPreview() {
    EnergyAppTheme {
        EmailInputScreen(
            isActivation = false,
            email = "test@email.com",
            isEmailValid = true,
            isLegalChecked = true,
            onEmailChange = {},
            onLegalChange = {},
            progress = 0.5f,
            onNext = {},
            onBack = {},
            onClose = {}
        )
    }
}

@Preview(showBackground = true, name = "Step 3 - OTP")
@Composable
fun OtpPreview() {
    EnergyAppTheme {
        OtpVerificationScreen(
            isActivation = true,
            otpCode = "123456",
            resendState = 0,
            progress = 0.75f,
            onOtpChange = {},
            onResendClick = {},
            otpAttemptsLeft = 2,
            hasRequestedResend = true,
            onNext = {},
            onBack = {},
            onClose = {},
            onCloseNotice = {},
            verSoporte = false,
            isOtpValid = true,
        )
    }
}

@Preview(showBackground = true, name = "Step 3 - OTP Reenviado")
@Composable
fun OtpResentPreview() {
    EnergyAppTheme {
        OtpVerificationScreen(
            isActivation = true,
            otpCode = "123456",
            resendState = 2,
            progress = 0.75f,
            onOtpChange = {},
            onResendClick = {},
            otpAttemptsLeft = 0,
            hasRequestedResend = true,
            onNext = {},
            onBack = {},
            onClose = {},
            onCloseNotice = {},
            verSoporte = true,
            isOtpValid = true,
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