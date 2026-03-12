package com.iberdrola.practicas2026.presentation.ui.invoice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iberdrola.practicas2026.presentation.composables.home.HomeScreen
import com.iberdrola.practicas2026.presentation.composables.invoice.InvoiceScreen
import com.iberdrola.practicas2026.presentation.ui.theme.EnergyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EnergyAppTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            onNavigateToInvoices = { navController.navigate("invoices") }
                        )
                    }
                    composable("invoices") {
                        InvoiceScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}