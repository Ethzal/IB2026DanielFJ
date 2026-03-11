package com.iberdrola.practicas2026.domain.usecase

import com.iberdrola.practicas2026.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.Flow
import com.iberdrola.practicas2026.domain.model.InvoiceResponse

class GetInvoicesUseCase (
    private val repository: InvoiceRepository
) {
    suspend operator fun invoke(isLocal: Boolean): Flow<InvoiceResponse> {
        return repository.getInvoices(isLocal)
    }
}