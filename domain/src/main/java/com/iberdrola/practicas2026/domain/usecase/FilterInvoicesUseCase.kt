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
        // 1. Filtramos primero por tipo (todos los de Luz o Gas)
        val invoicesForType = allInvoices.filter { it.type == type.value }

        // 2. Calculamos la Última Factura (Permanente, independiente de filtros)
        val absoluteLastInvoice = invoicesForType.maxByOrNull {
            try {
                LocalDate.parse(it.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            } catch (_: Exception) {
                LocalDate.MIN // Si falla el formato, asume fecha mínima
            }
        }

        var filtered = invoicesForType

        // 3. Aplicamos criterios de filtro (fecha, importe, estado) a la lista histórica
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

        // 4. Ordenar el histórico de más nueva a más antigua
        val sortedList = filtered.sortedByDescending {
            try {
                LocalDate.parse(it.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            } catch (_: Exception) {
                LocalDate.MIN // Si falla el formato, lo pone al final
            }
        }

        // 5. Devolvemos el response con la card permanente y la lista histórica filtrada
        return InvoiceResponse(
            lastInvoice = absoluteLastInvoice,
            history = sortedList,
            allInvoices = allInvoices
        )
    }
}