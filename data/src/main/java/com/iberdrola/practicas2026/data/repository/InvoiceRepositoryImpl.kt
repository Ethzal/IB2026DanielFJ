package com.iberdrola.practicas2026.data.repository

import com.iberdrola.practicas2026.data.local.InvoiceDao
import com.iberdrola.practicas2026.data.remote.InvoiceApi
import com.iberdrola.practicas2026.data.di.LocalApi
import com.iberdrola.practicas2026.data.di.RemoteApi
import com.iberdrola.practicas2026.data.local.InvoiceEntity
import com.iberdrola.practicas2026.domain.model.*
import com.iberdrola.practicas2026.domain.repository.InvoiceRepository
import com.iberdrola.practicas2026.data.remote.InvoiceResponseDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InvoiceRepositoryImpl @Inject constructor(
    @param:LocalApi private val apiLocal: InvoiceApi,
    @param:RemoteApi private val apiRemote: InvoiceApi,
    private val dao: InvoiceDao
) : InvoiceRepository {

    override suspend fun getInvoices(isLocal: Boolean): Flow<InvoiceResponse> = flow {
        try {
            val dto: InvoiceResponseDto = if (isLocal) {
                delay((1000..3000).random().toLong())
                apiLocal.getInvoices()
            } else {
                val remoteDto = apiRemote.getInvoices()

                remoteDto.history?.let { list ->
                    val entities = list.map {
                        InvoiceEntity(
                            it.id ?: "",
                            it.date ?: "",
                            it.type ?: "",
                            it.amount ?: 0.0,
                            it.status ?: ""
                        )
                    }
                    dao.saveInvoices(entities)
                }
                remoteDto
            }

            emit(mapDtoToDomain(dto))
        } catch (e: Exception) {
            throw e
        }
    }

    private fun mapDtoToDomain(dto: InvoiceResponseDto): InvoiceResponse {
        return InvoiceResponse(
            lastInvoice = InvoiceDetail(
                id = dto.lastInvoice?.id ?: "",
                type = dto.lastInvoice?.type ?: "",
                amount = dto.lastInvoice?.amount ?: 0.0,
                startDate = dto.lastInvoice?.startDate ?: "",
                endDate = dto.lastInvoice?.endDate ?: "",
                status = dto.lastInvoice?.status ?: ""
            ),
            history = dto.history?.map {
                InvoiceItem(
                    id = it.id ?: "",
                    date = it.date ?: "",
                    type = it.type ?: "",
                    amount = it.amount ?: 0.0,
                    status = it.status ?: ""
                )
            } ?: emptyList()
        )
    }
}