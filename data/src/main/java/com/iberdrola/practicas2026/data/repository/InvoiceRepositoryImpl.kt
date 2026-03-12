package com.iberdrola.practicas2026.data.repository

import com.iberdrola.practicas2026.data.local.InvoiceDao
import com.iberdrola.practicas2026.data.remote.InvoiceApi
import com.iberdrola.practicas2026.data.di.LocalApi
import com.iberdrola.practicas2026.data.di.RemoteApi
import com.iberdrola.practicas2026.data.local.InvoiceEntity
import com.iberdrola.practicas2026.data.remote.InvoiceItemDto
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
        if (isLocal) {
            // MODO LOCAL
            delay((1000..3000).random().toLong())
            val dto = apiLocal.getInvoices()
            emit(mapDtoToDomain(dto))
        } else {
            // MODO REMOTO: Red -> Room -> UI
            try {
                val remoteDto = apiRemote.getInvoices()

                // Preparamos la lista para Room
                val entities = mutableListOf<InvoiceEntity>()

                // 1. Guardamos la última factura
                remoteDto.lastInvoice?.let {
                    entities.add(InvoiceEntity(
                        id = it.id ?: "LAST_INV_ID",
                        date = "",
                        type = it.type ?: "",
                        amount = it.amount ?: 0.0,
                        status = it.status ?: "",
                        isLastInvoice = true,
                        startDate = it.startDate ?: "",
                        endDate = it.endDate ?: ""
                    ))
                }

                // 2. Guardamos el historial
                remoteDto.history?.forEach {
                    entities.add(InvoiceEntity(
                        id = it.id ?: "",
                        date = it.date ?: "",
                        type = it.type ?: "",
                        amount = it.amount ?: 0.0,
                        status = it.status ?: "",
                        isLastInvoice = false
                    ))
                }

                dao.clearInvoices()
                dao.saveInvoices(entities)

            } catch (e: Exception) {
                println("ERROR REMOTO: ${e.message}")
            }

            // LEER DE ROOM
            val dbInvoices = dao.getAllInvoices()

            if (dbInvoices.isEmpty()) {
                throw Exception("No hay datos en caché y la red falló")
            }

            val lastEntity = dbInvoices.find { it.isLastInvoice }
            val historyEntities = dbInvoices.filter { !it.isLastInvoice }

            emit(InvoiceResponse(
                lastInvoice = InvoiceDetail(
                    id = lastEntity?.id ?: "",
                    type = lastEntity?.type ?: "",
                    amount = lastEntity?.amount ?: 0.0,
                    startDate = lastEntity?.startDate ?: "",
                    endDate = lastEntity?.endDate ?: "",
                    status = lastEntity?.status ?: ""
                ),
                history = historyEntities.map {
                    InvoiceItem(it.id, it.date, it.type, it.amount, it.status)
                }
            ))
        }
    }

    // Función auxiliar para crear el detalle desde la primera entidad de Room
    private fun mapEntityToDetail(entity: InvoiceEntity?): InvoiceDetail {
        return entity?.let {
            InvoiceDetail(
                id = it.id,
                type = it.type,
                amount = it.amount,
                startDate = "",
                endDate = "",
                status = it.status
            )
        } ?: InvoiceDetail("", "", 0.0, "", "", "")
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

    // Mappers: Entity -> Domain
    fun InvoiceEntity.toDomain() = InvoiceItem(
        id = id,
        date = date,
        type = type,
        amount = amount,
        status = status
    )

    // Mappers: DTO -> Entity
    fun InvoiceItemDto.toEntity() = InvoiceEntity(
        id = id ?: "",
        date = date ?: "",
        type = type ?: "",
        amount = amount ?: 0.0,
        status = status ?: ""
    )
}