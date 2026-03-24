package com.iberdrola.practicas2026.domain.usecase

import com.iberdrola.practicas2026.domain.model.InvoiceFilter
import com.iberdrola.practicas2026.domain.model.Invoice
import com.iberdrola.practicas2026.domain.model.InvoiceResponse
import com.iberdrola.practicas2026.domain.model.InvoiceType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FilterInvoicesUseCase {
    operator fun invoke(
        allInvoices: List<Invoice>,
        type: InvoiceType,
        criteria: InvoiceFilter? = null
    ): InvoiceResponse {
        var filtered = allInvoices.filter { it.type == type.value }

        criteria?.let { filter ->
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            // Filtrar por Fecha
            filter.dateFrom?.let { from ->
                val fromDate = LocalDate.parse(from, formatter)
                filtered = filtered.filter { LocalDate.parse(it.date, formatter) >= fromDate }
            }
            filter.dateTo?.let { to ->
                val toDate = LocalDate.parse(to, formatter)
                filtered = filtered.filter { LocalDate.parse(it.date, formatter) <= toDate }
            }

            // Filtrar por Importe
            filter.amountRange?.let { range ->
                filtered = filtered.filter { it.amount.toFloat() in range }
            }

            // Filtrar por Estado
            if (filter.statuses.isNotEmpty()) {
                filtered = filtered.filter { filter.statuses.contains(it.status) }
            }
        }

        return InvoiceResponse(
            lastInvoice = filtered.firstOrNull(),
            history = if (filtered.size > 1) filtered.drop(1) else emptyList(),
            allInvoices = allInvoices
        )
    }
}