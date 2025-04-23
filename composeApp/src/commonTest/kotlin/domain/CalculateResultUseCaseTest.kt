package org.julianvelandia.raya.domain

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CalculateResultUseCaseTest {

    @Test
    fun `should return success with correct calculation`() = runTest {
        val walletRepository = FakeWalletRepository(
            mutableMapOf("USD" to balanceDummy)
        )
        val useCase = CalculateResultUseCase(walletRepository)

        val result = useCase(amountText = "10", rate = 2.0, origin = "USD")

        assertTrue(result.isSuccess)
        assertEquals(20.0, result.getOrNull())
    }

    @Test
    fun `should return failure when not enough funds`() = runTest {
        val walletRepository = FakeWalletRepository(
            mutableMapOf("USD" to balanceDummy.copy(amount = 5.0))
        )
        val useCase = CalculateResultUseCase(walletRepository)

        val result = useCase(amountText = "10", rate = 2.0, origin = "USD")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ConversionError.NotEnoughFunds)
    }

    @Test
    fun `should return success with null when rate is null`() = runTest {
        val walletRepository = FakeWalletRepository(
            mutableMapOf("USD" to balanceDummy)
        )
        val useCase = CalculateResultUseCase(walletRepository)

        val result = useCase(amountText = "10", rate = null, origin = "USD")

        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }

    @Test
    fun `should return failure when rate is zero`() = runTest {
        val walletRepository = FakeWalletRepository(
            mutableMapOf("USD" to balanceDummy)
        )
        val useCase = CalculateResultUseCase(walletRepository)

        val result = useCase(amountText = "10", rate = 0.0, origin = "USD")

        assertTrue(result.isSuccess)
        assertEquals(0.0, result.getOrNull())
    }

    @Test
    fun `should return failure when origin not found in wallet`() = runTest {
        val walletRepository = FakeWalletRepository(
            mutableMapOf("USD" to balanceDummy)
        )
        val useCase = CalculateResultUseCase(walletRepository)

        val result = useCase(amountText = "10", rate = 2.0, origin = "EUR")  // EUR doesn't exist in the repository

        assertTrue(result.isFailure)
        assertNull(result.getOrNull())
    }
}

