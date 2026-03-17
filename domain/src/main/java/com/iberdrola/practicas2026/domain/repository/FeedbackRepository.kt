package com.iberdrola.practicas2026.domain.repository

import kotlinx.coroutines.flow.Flow

interface FeedbackRepository {
    fun shouldShowFeedback(): Flow<Boolean>
    suspend fun incrementExitCount()
    suspend fun setNextShowDelay(delay: Int)
}