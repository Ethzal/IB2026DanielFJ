package com.iberdrola.practicas2026.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [InvoiceEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun invoiceDao(): InvoiceDao
}