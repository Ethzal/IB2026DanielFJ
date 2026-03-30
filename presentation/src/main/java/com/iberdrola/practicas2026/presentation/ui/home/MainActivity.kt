package com.iberdrola.practicas2026.presentation.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iberdrola.practicas2026.presentation.composables.home.HomeScreen
import com.iberdrola.practicas2026.presentation.composables.invoice.InvoiceScreen
import com.iberdrola.practicas2026.presentation.navigation.Screen
import com.iberdrola.practicas2026.presentation.ui.theme.DarkGray
import com.iberdrola.practicas2026.presentation.ui.theme.EnergyAppTheme
import com.iberdrola.practicas2026.presentation.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EnergyAppTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                // Obtenemos el MainViewModel a nivel de Actividad para que sea global
                val mainViewModel: MainViewModel = hiltViewModel()

                // Escuchamos los eventos de mensaje
                val context = LocalContext.current

                LaunchedEffect(Unit) {
                    mainViewModel.uiEvent.collect { msgResId ->
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            message = context.getString(msgResId),
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState) { data ->
                            Snackbar(
                                snackbarData = data,
                                containerColor = DarkGray.copy(alpha = 0.9f),
                                contentColor = White
                            )
                        }
                    }
                ) { innerPadding ->

                    Box(modifier = Modifier.fillMaxSize()) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Home.route,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            composable(Screen.Home.route) {
                                HomeScreen(
                                    mainViewModel = mainViewModel,
                                    onNavigateToInvoices = { navController.navigate(Screen.Invoices.route) }
                                )
                            }
                            composable(Screen.Invoices.route) {
                                InvoiceScreen(
                                    onBackClick = {
                                        if (navController.previousBackStackEntry != null) {
                                            navController.popBackStack()
                                        }
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}