package com.iberdrola.practicas2026.data.remote

import com.google.gson.annotations.SerializedName

data class InvoiceResponseDto(
    @SerializedName("ultima_factura") val lastInvoice: InvoiceDetailDto?,
    @SerializedName("historico") val history: List<InvoiceItemDto>?
)

data class InvoiceDetailDto(
    @SerializedName("id") val id: String?,
    @SerializedName("tipo") val type: String?,
    @SerializedName("importe") val amount: Double?,
    @SerializedName("fecha_inicio") val startDate: String?,
    @SerializedName("fecha_fin") val endDate: String?,
    @SerializedName("estado") val status: String?
)

data class InvoiceItemDto(
    @SerializedName("id") val id: String?,
    @SerializedName("fecha") val date: String?,
    @SerializedName("tipo") val type: String?,
    @SerializedName("importe") val amount: Double?,
    @SerializedName("estado") val status: String?
)