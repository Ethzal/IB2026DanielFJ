package com.iberdrola.practicas2026.domain.usecase

import com.iberdrola.practicas2026.domain.repository.FeedbackRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetFeedbackStatusUseCaseTest {

    private lateinit var repository: FeedbackRepository
    private lateinit var useCase: GetFeedbackStatusUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetFeedbackStatusUseCase(repository)
    }

    @Test
    fun `should return feedback status from repository`() = runTest {
        // Given
        val expected = true
        every { repository.shouldShowFeedback() } returns flowOf(expected)

        // When
        val resultFlow = useCase().first()

        // Then
        assertEquals(expected, resultFlow)

    }
}