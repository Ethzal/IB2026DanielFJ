package com.iberdrola.practicas2026.data.repository

import com.iberdrola.practicas2026.core.di.LocalApi
import com.iberdrola.practicas2026.core.di.RemoteApi
import com.iberdrola.practicas2026.data.local.InvoiceDao
import com.iberdrola.practicas2026.data.local.InvoiceEntity
import com.iberdrola.practicas2026.data.mapper.toInvoiceStatus
import com.iberdrola.practicas2026.domain.model.Invoice
import com.iberdrola.practicas2026.domain.model.InvoiceResponse
import com.iberdrola.practicas2026.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.domain.repository.InvoiceRepository
import com.iberdrola.practicas2026.data.remote.InvoiceApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InvoiceRepositoryImpl @Inject constructor(
    @param:LocalApi private val apiLocal: InvoiceApi,
    @param:RemoteApi private val apiRemote: InvoiceApi,
    private val dao: InvoiceDao
) : InvoiceRepository {

    override suspend fun getInvoices(isLocal: Boolean): Flow<InvoiceResponse> {
        if (isLocal) {
            return flow {
                delay((1000..3000).random().toLong())
                val dto = apiLocal.getInvoices()

                val domainList = dto.facturas?.map {
                    Invoice(
                        id = it.id ?: "",
                        date = it.date ?: "",
                        type = it.type ?: "",
                        amount = it.amount ?: 0.0,
                        status = InvoiceStatus.fromId(it.status),
                        startDate = it.startDate ?: "",
                        endDate = it.endDate ?: ""
                    )
                } ?: emptyList()

                emit(InvoiceResponse(allInvoices = domainList))
            }
        } else {
            return flow {
                val initialData = dao.getAllInvoices()

                try {
                    delay((1000..3000).random().toLong())
                    val remoteDto = apiRemote.getInvoices()

                    val entities = remoteDto.facturas?.mapIndexed { index, item ->
                        InvoiceEntity(
                            id = item.id ?: "ID_$index",
                            date = item.date ?: "",
                            type = item.type ?: "",
                            amount = item.amount ?: 0.0,
                            status = (item.status ?: "").toInvoiceStatus().id,
                            startDate = item.startDate ?: "",
                            endDate = item.endDate ?: "",
                            isLastInvoice = index == 0
                        )
                    } ?: emptyList()

                    if (entities.isNotEmpty()) {
                        dao.replaceAllInvoices(entities)
                    } else if (initialData.isEmpty()) {
                        throw Exception("Error de conexión y no hay datos offline.")
                    }
                } catch (_: Exception) {
                    if (initialData.isEmpty()) {
                        throw Exception("Error de conexión y no hay datos offline.")
                    }
                }

                emitAll(dao.observeAllInvoices().map { entities ->
                    InvoiceResponse(allInvoices = entities.map { it.toDomain() })
                })
            }
        }
    }

    private fun InvoiceEntity.toDomain() = Invoice(
        id = id,
        date = date,
        type = type,
        amount = amount,
        status = InvoiceStatus.fromId(status),
        startDate = startDate,
        endDate = endDate
    )
}