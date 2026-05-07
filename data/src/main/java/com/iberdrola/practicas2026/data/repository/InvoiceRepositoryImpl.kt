package com.iberdrola.practicas2026.data.repository

import com.iberdrola.practicas2026.core.di.LocalApi
import com.iberdrola.practicas2026.core.di.RemoteApi
import com.iberdrola.practicas2026.data.local.InvoiceDao
import com.iberdrola.practicas2026.data.remote.InvoiceApi
import com.iberdrola.practicas2026.data.local.InvoiceEntity
import com.iberdrola.practicas2026.data.mapper.toInvoiceStatus
import com.iberdrola.practicas2026.data.remote.InvoiceItemDto
import com.iberdrola.practicas2026.domain.model.*
import com.iberdrola.practicas2026.domain.repository.InvoiceRepository
import com.iberdrola.practicas2026.data.remote.InvoiceResponseDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class InvoiceRepositoryImpl @Inject constructor(
    @param:LocalApi private val apiLocal: InvoiceApi,
    @param:RemoteApi private val apiRemote: InvoiceApi,
    private val dao: InvoiceDao
) : InvoiceRepository {

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override suspend fun getInvoices(isLocal: Boolean): Flow<InvoiceResponse> {
        repositoryScope.launch {
            try {
                if (isLocal) delay((1000..3000).random().toLong())
                val api = if (isLocal) apiLocal else apiRemote
                val responseDto = api.getInvoices()

                val entities = responseDto.facturas?.mapIndexed { index, item ->
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
                    dao.clearAndSave(entities) // Función transaccional que comentamos antes
                }
            } catch (e: Exception) {
                // Error de red: No hacemos nada, Room emitirá lo que tenga (vacío o viejo)
            }
        }

            // Devolvemos el flujo de Room
        return dao.observeAllInvoices().map { entities ->
//                if (entities.isEmpty()) {
//                    throw Exception("Error de conexión y no hay datos offline.")
//                }
            InvoiceResponse(allInvoices = entities.map { it.toDomain() })
        }

    }

    private fun mapDtoToDomain(dto: InvoiceResponseDto): InvoiceResponse {
        // Mapeamos la lista de DTOs a lista de Dominio
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

        return InvoiceResponse(allInvoices = domainList)
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

    // Mappers: DTO -> Entity
    fun InvoiceItemDto.toEntity() = InvoiceEntity(
        id = id ?: "",
        date = date ?: "",
        type = type ?: "",
        amount = amount ?: 0.0,
        status = status ?: ""
    )
}