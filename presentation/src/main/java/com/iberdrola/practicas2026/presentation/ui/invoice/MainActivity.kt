package com.iberdrola.practicas2026.presentation.ui.invoice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iberdrola.practicas2026.presentation.composables.home.HomeScreen
import com.iberdrola.practicas2026.presentation.composables.invoice.InvoiceScreen
import com.iberdrola.practicas2026.presentation.navigation.Screen
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

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route
                    ) {
                        // Pantalla de Inicio
                        composable(Screen.Home.route) {
                            HomeScreen(
                                onNavigateToInvoices = {
                                    navController.navigate(Screen.Invoices.route)
                                }
                            )
                        }

                        // Pantalla de Facturas
                        composable(Screen.Invoices.route) {
                            InvoiceScreen(
                                onBackClick = {
                                    navController.popBackStack()
                                    // Implementar: Bottom Sheet feedback
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}