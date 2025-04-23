package org.julianvelandia.raya.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext


class PerformConversionUseCase(private val walletRepository: WalletRepository) {

    suspend fun execute(
        originCurrency: String,
        originAmount: Double,
        destCurrency: String,
        destAmount: Double
    ): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {

                val originBalanceEntity = walletRepository.getBalanceByCode(originCurrency)
                val currentOriginAmount = originBalanceEntity?.amount ?: 0.0
                val originFlag = originBalanceEntity?.flag.orEmpty()


                if (currentOriginAmount < originAmount) {
                    throw IllegalStateException("Fondos insuficientes para $originCurrency")
                }

                val destBalanceEntity = walletRepository.getBalanceByCode(destCurrency)
                val currentDestAmount = destBalanceEntity?.amount ?: 0.0
                val destFlag = destBalanceEntity?.flag ?: ""

                val newOriginAmount = currentOriginAmount - originAmount
                val newDestAmount = currentDestAmount + destAmount

                walletRepository.insertOrUpdateBalance(
                    Balance(
                        currencyCode = originCurrency,
                        flag = originFlag,
                        amount = newOriginAmount
                    )
                )

                walletRepository.insertOrUpdateBalance(
                    Balance(
                        currencyCode = destCurrency,
                        flag = destFlag,
                        amount = newDestAmount
                    )
                )

                walletRepository.insertTransaction(
                    sourceCurrencyCode = originCurrency,
                    sourceAmount = originAmount,
                    targetCurrencyCode = destCurrency,
                    targetAmount = destAmount,
                )

            }

            Result.success(Unit)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}