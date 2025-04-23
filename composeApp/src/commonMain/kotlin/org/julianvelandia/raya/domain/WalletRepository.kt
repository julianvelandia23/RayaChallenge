package org.julianvelandia.raya.domain

import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    fun getBalance(): Flow<Result<List<Balance>>>
    fun getTransactions(): Flow<Result<List<Transaction>>>
    suspend fun insertOrUpdateBalance(balance: Balance): Result<Unit>
    suspend fun getBalanceByCode(currencyCode: String): Balance?
    suspend fun insertTransaction(
        sourceCurrencyCode: String,
        sourceAmount: Double,
        targetCurrencyCode: String,
        targetAmount: Double
    ): Result<Unit>
}