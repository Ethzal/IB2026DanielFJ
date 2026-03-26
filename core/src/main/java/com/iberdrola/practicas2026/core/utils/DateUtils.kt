package com.iberdrola.practicas2026.core.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.toUiDate(): String {
    return try {
        val parsed = LocalDate.parse(this)
        val formatter = DateTimeFormatter.ofPattern("d 'de' MMMM", Locale.forLanguageTag("es-ES"))
        parsed.format(formatter)
    } catch (_: Exception) {
        this // Si falla, devuelve el original
    }
}
fun String.toLastInvoiceDate(): String {
    return try {
        val parsed = LocalDate.parse(this)
        val formatter = DateTimeFormatter.ofPattern("dd MMM'.' y", Locale.forLanguageTag("es-ES"))
        parsed.format(formatter)
    } catch (_: Exception) {
        this // Si falla, devuelve el original
    }
}
fun String?.toEpochMillis(): Long? {
    return try {
        this?.let {
            LocalDate.parse(it)
                .atStartOfDay(java.time.ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli()
        }
    } catch (_: Exception) {
        null
    }
}
