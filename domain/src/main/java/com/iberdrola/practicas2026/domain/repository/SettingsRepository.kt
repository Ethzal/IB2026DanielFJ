package com.iberdrola.practicas2026.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun isLocalMode(): Flow<Boolean>
    suspend fun toggleLocalMode(isLocal: Boolean)
}