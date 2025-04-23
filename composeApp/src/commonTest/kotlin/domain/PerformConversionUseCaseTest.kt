package org.julianvelandia.raya.domain

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PerformConversionUseCaseTest {

    @Test
    fun `should perform successful conversion and update balances`() = runTest {
        val walletRepository = FakeWalletRepository(
            mutableMapOf(
                "USD" to Balance("USD", "US", 100.0),
                "EUR" to Balance("EUR", "EU", 50.0)
            )
        )
        val useCase = PerformConversionUseCase(walletRepository)

        val result = useCase.execute(originCurrency = "USD", originAmount = 30.0, destCurrency = "EUR", destAmount = 25.0)

        assertTrue(result.isSuccess)

        val updatedOriginBalance = walletRepository.getBalanceByCode("USD")?.amount
        val updatedDestBalance = walletRepository.getBalanceByCode("EUR")?.amount

        assertEquals(70.0, updatedOriginBalance)
        assertEquals(75.0, updatedDestBalance)
    }

    @Test
    fun `should fail when origin balance is insufficient`() = runTest {
        val walletRepository = FakeWalletRepository(
            mutableMapOf("USD" to Balance("USD", "US", 20.0))
        )
        val useCase = PerformConversionUseCase(walletRepository)

        val result = useCase.execute(originCurrency = "USD", originAmount = 30.0, destCurrency = "EUR", destAmount = 25.0)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalStateException)
        assertEquals("Fondos insuficientes para USD", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should return failure when origin currency does not exist`() = runTest {
        val walletRepository = FakeWalletRepository(
            mutableMapOf("USD" to Balance("USD", "US", 100.0))
        )
        val useCase = PerformConversionUseCase(walletRepository)

        val result = useCase.execute(originCurrency = "ARS", originAmount = 100.0, destCurrency = "BTC", destAmount = 50.0)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalStateException)
        assertEquals("Fondos insuficientes para ARS", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should return success with empty destination balance when destination currency does not exist`() = runTest {
        val walletRepository = FakeWalletRepository(
            mutableMapOf("USD" to Balance("USD", "US", 100.0))
        )
        val useCase = PerformConversionUseCase(walletRepository)

        val result = useCase.execute(originCurrency = "USD", originAmount = 50.0, destCurrency = "EUR", destAmount = 50.0)

        assertTrue(result.isSuccess)
    }



}
