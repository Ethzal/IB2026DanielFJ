package com.iberdrola.practicas2026.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Invoices : Screen("invoices")
}