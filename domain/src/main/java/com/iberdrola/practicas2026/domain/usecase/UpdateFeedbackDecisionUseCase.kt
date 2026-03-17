package com.iberdrola.practicas2026.domain.usecase

import com.iberdrola.practicas2026.domain.repository.FeedbackRepository

class UpdateFeedbackDecisionUseCase(private val repository: FeedbackRepository) {
    suspend fun incrementExit() = repository.incrementExitCount()
    
    suspend fun setDelay(isRated: Boolean) {
        val delay = if (isRated) 10 else 3
        repository.setNextShowDelay(delay)
    }
}