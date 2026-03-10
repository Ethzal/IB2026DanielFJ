package com.iberdrola.practicas2026.data.repository
import com.iberdrola.practicas2026.data.local.InvoiceDao
import com.iberdrola.practicas2026.data.remote.InvoiceApi
import com.iberdrola.practicas2026.domain.model.InvoiceDetail
import com.iberdrola.practicas2026.domain.model.InvoiceItem
import com.iberdrola.practicas2026.domain.model.InvoiceResponse
import com.iberdrola.practicas2026.domain.repository.InvoiceRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InvoiceRepositoryImpl @Inject constructor(
    private val api: InvoiceApi,
    private val dao: InvoiceDao
) : InvoiceRepository {

    override suspend fun getInvoices(isLocal: Boolean): Flow<InvoiceResponse> = flow {
        if (isLocal) {
            delay((1000..3000).random().toLong())
            emit(getLocalMock())
        } else {
            val response = api.getRemoteInvoices()
            emit(response)
        }
    }

    private fun getLocalMock() = InvoiceResponse(
        lastInvoice = InvoiceDetail("F1", "Factura Luz", 20.0, "01 feb", "04 mar", "Pendiente"),
        history = listOf(InvoiceItem("F2", "8 marzo", "Factura Luz", 20.0, "Pagada"))
    )
}