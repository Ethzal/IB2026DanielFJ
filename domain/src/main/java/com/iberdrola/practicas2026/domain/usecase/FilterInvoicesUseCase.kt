package com.iberdrola.practicas2026.domain.usecase

import com.iberdrola.practicas2026.domain.model.Invoice
import com.iberdrola.practicas2026.domain.model.InvoiceType
import com.iberdrola.practicas2026.domain.model.InvoiceResponse

class FilterInvoicesUseCase {
    operator fun invoke(allInvoices: List<Invoice>, type: InvoiceType): InvoiceResponse {
        val filtered = allInvoices.filter { it.type == type.value }
        
        return InvoiceResponse(
            lastInvoice = filtered.firstOrNull(),
            history = if (filtered.size > 1) filtered.drop(1) else emptyList(),
            allInvoices = allInvoices // Mantenemos la lista completa original
        )
    }
}