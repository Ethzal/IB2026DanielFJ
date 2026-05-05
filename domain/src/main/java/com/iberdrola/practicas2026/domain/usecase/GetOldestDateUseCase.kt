package com.iberdrola.practicas2026.domain.usecase

import com.iberdrola.practicas2026.domain.model.Invoice
import com.iberdrola.practicas2026.domain.model.InvoiceStatus
import java.time.LocalDate
import java.time.ZoneOffset

class GetOldestDateUseCase {
    operator fun invoke(
        allInvoices: List<Invoice>,
        amountRange: ClosedFloatingPointRange<Float>,
        selectedStatuses: Set<InvoiceStatus>
    ): Long? {
        val oldestDateString = allInvoices.filter { invoice ->
            val isInAmount = invoice.amount.toFloat() in amountRange
            val isStatusSelected = selectedStatuses.isEmpty() || selectedStatuses.contains(invoice.status)
            isInAmount && isStatusSelected
        }.minOfOrNull { it.date }

        return try {
            oldestDateString?.let {
                LocalDate.parse(it)
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant()
                    .toEpochMilli()
            }
        } catch (_: Exception) {
            null
        }
    }
}