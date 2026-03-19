package com.iberdrola.practicas2026.domain.usecase

import com.iberdrola.practicas2026.domain.repository.FeedbackRepository
import org.junit.Test

import io.mockk.mockk
import io.mockk.coVerify

import kotlinx.coroutines.test.runTest

class UpdateFeedbackDecisionUseCaseTest {
    private val repository = mockk<FeedbackRepository>(relaxed = true)
    private val useCase = UpdateFeedbackDecisionUseCase(repository)

    @Test
    fun `incrementExit calls repository increment`() = runTest {
        // When
        useCase.incrementExit()

        // Then
        coVerify { repository.incrementExitCount() }
    }

    @Test
    fun `setDelay with isRated true sets delay to 10`() = runTest {
        // Given
        val isRated = true

        // When
        useCase.setDelay(isRated)

        // Then
        coVerify { repository.setNextShowDelay(10) }
    }

    @Test
    fun `setDelay with isRated false sets delay to 3`() = runTest {
        // Given
        val isRated = false

        // When
        useCase.setDelay(isRated)

        // Then
        coVerify { repository.setNextShowDelay(3) }
    }
}