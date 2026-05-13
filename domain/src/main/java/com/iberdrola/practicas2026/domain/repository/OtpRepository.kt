package com.iberdrola.practicas2026.domain.repository

import kotlinx.coroutines.flow.Flow

interface OtpRepository {
    val otpAttempts: Flow<Pair<Int, Long>>

    suspend fun saveOtpAttempts(attempts: Int, lastTime: Long)
}