package com.iberdrola.practicas2026.data.local

@androidx.room.Dao
interface InvoiceDao {
    @androidx.room.Query("SELECT * FROM invoices")
    fun getAllInvoices(): kotlinx.coroutines.flow.Flow<List<InvoiceEntity>>

    @androidx.room.Upsert
    suspend fun saveInvoices(invoices: List<InvoiceEntity>)
}