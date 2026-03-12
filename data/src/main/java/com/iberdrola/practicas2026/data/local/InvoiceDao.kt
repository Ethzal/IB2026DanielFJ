package com.iberdrola.practicas2026.data.local

@androidx.room.Dao
interface InvoiceDao {
    @androidx.room.Query("SELECT * FROM invoices")
    fun observeAllInvoices(): kotlinx.coroutines.flow.Flow<List<InvoiceEntity>>

    @androidx.room.Query("SELECT * FROM invoices")
    suspend fun getAllInvoices(): List<InvoiceEntity>

    @androidx.room.Upsert
    suspend fun saveInvoices(invoices: List<InvoiceEntity>)

    @androidx.room.Query("DELETE FROM invoices")
    suspend fun clearInvoices()
}