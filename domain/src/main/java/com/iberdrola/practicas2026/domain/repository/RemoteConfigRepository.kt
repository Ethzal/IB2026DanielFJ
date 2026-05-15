package com.iberdrola.practicas2026.domain.repository
import kotlinx.coroutines.flow.StateFlow

interface RemoteConfigRepository {
    val isGasEnabled: StateFlow<Boolean>
    suspend fun fetchAndActivate()
}