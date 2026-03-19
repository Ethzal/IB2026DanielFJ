package com.iberdrola.practicas2026.domain.usecase

import com.iberdrola.practicas2026.domain.model.InvoiceResponse
import com.iberdrola.practicas2026.domain.repository.InvoiceRepository
import org.junit.Test
import org.junit.Assert.assertEquals

import io.mockk.mockk
import io.mockk.coEvery
import kotlinx.coroutines.flow.first

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before

class GetInvoicesUseCaseTest {

    private lateinit var repository: InvoiceRepository
    private lateinit var useCase: GetInvoicesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetInvoicesUseCase(repository)
    }

    @Test
    fun `should return invoices from repository`() = runTest {
        // Given
        val expected = InvoiceResponse(/* mock data */)

        coEvery { repository.getInvoices(true) } returns flowOf(expected)

        // When
        val result = useCase(true).first()

        // Then
        assertEquals(expected, result)
    }
}