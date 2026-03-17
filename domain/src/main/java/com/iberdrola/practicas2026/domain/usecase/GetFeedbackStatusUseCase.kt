package com.iberdrola.practicas2026.domain.usecase

import com.iberdrola.practicas2026.domain.repository.FeedbackRepository

class GetFeedbackStatusUseCase(private val repository: FeedbackRepository) {
    operator fun invoke() = repository.shouldShowFeedback()
}