package com.iberdrola.practicas2026.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Invoices : Screen("invoices")
    object ElectronicInvoiceList : Screen("electronic_invoice_list")
    object ModifyEmailInfo : Screen("modify_email_info")
    object EmailInput : Screen("email_input/{isActivation}") {
        fun createRoute(isActivation: Boolean) = "email_input/$isActivation"
    }
    object OtpVerification : Screen("otp_verification/{isActivation}") {
        fun createRoute(isActivation: Boolean) = "otp_verification/$isActivation"
    }
    object Success : Screen("success/{isActivation}") {
        fun createRoute(isActivation: Boolean) = "success/$isActivation"
    }
}