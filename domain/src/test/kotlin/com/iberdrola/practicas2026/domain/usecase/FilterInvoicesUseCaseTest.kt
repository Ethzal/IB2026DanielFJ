package com.iberdrola.practicas2026.domain.usecase

import com.iberdrola.practicas2026.domain.model.Invoice
import com.iberdrola.practicas2026.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.domain.model.InvoiceType
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

class FilterInvoicesUseCaseTest {
    private val useCase = FilterInvoicesUseCase()

    @Test
    fun `when list is filtered by LIGHT then lastInvoice is the first light invoice`() {
        // Given
        val invoices = listOf(
            Invoice(id = "1", date = "2024-03-08", type = "Factura Luz", amount = 10.0, status = InvoiceStatus.Pending),
            Invoice(id = "2", date = "2024-02-06", type = "Factura Luz", amount = 20.0, status = InvoiceStatus.Paid),
            Invoice(id = "3", date = "2024-02-03", type = "Factura Gas", amount = 30.0, status = InvoiceStatus.Paid)
        )

        // When
        val result = useCase(invoices, InvoiceType.LIGHT)

        // Then
        assertEquals("1", result.lastInvoice?.id)
        assertEquals(2, result.history.size)
        assertEquals("1", result.history[0].id)
        assertEquals("2", result.history[1].id)
    }

    @Test
    fun `when no invoices match type then response is empty`() {
        // Given
        val invoices = listOf(
            Invoice(id = "1", date = "2024-01-01", type = "Factura Gas", amount = 0.0, status = InvoiceStatus.Paid)
        )

        // When
        val result = useCase(invoices, InvoiceType.LIGHT)

        // Then
        assertNull(result.lastInvoice)
        assertTrue(result.history.isEmpty())
    }

    @Test
    fun `should filter only light invoices preserving order`() {
        // Given
        val invoices = listOf(
            Invoice(id = "1", date = "2024-01-01", type = "Factura Gas", amount = 10.0, status = InvoiceStatus.Paid),
            Invoice(id = "2", date = "2024-02-01", type = "Factura Luz", amount = 20.0, status = InvoiceStatus.Paid),
            Invoice(id = "3", date = "2024-03-01", type = "Factura Luz", amount = 30.0, status = InvoiceStatus.Paid)
        )

        // When
        val result = useCase(invoices, InvoiceType.LIGHT)

        // Then
        assertEquals("3", result.lastInvoice?.id)
        assertEquals(2, result.history.size)
        assertEquals("3", result.history[0].id)
        assertEquals("2", result.history[1].id)
    }
}