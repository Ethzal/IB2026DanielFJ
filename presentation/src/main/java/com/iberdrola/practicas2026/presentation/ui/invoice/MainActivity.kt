package com.iberdrola.practicas2026.presentation.ui.invoice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
                InvoiceScreen(
                    onBackClick = {
                        // Bottom Sheet de valoración
                    }
                )
            }
        }
    }
}