package com.iberdrola.practicas2026.core.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Formatea un Double al estándar español: 1.234,56
 * Usamos Locale.forLanguageTag para evitar constructores obsoletos.
 */
fun Double.formatSpanishCurrency(): String {
    val spanishLocale = Locale.forLanguageTag("es-ES")

    val symbols = DecimalFormatSymbols(spanishLocale).apply {
        decimalSeparator = ','
        groupingSeparator = '.'
    }

    val formatter = DecimalFormat("#,##0.00", symbols)
    return formatter.format(this)
}

/**
 * Formato para el Slider sin decimales
 */
fun Float.formatSpanishInteger(): String {
    val spanishLocale = Locale.forLanguageTag("es-ES")

    val symbols = DecimalFormatSymbols(spanishLocale).apply {
        groupingSeparator = '.'
    }

    val formatter = DecimalFormat("#,###", symbols)
    return formatter.format(this.toInt())
}