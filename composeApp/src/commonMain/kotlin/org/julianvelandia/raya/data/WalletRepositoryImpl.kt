package org.julianvelandia.raya.data

import kotlinx.coroutines.flow.Flow
import org.julianvelandia.raya.domain.Balance
import org.julianvelandia.raya.domain.Transaction
import org.julianvelandia.raya.domain.WalletRepository

class WalletRepositoryImpl(
    private val localBalanceDataSource: LocalBalanceDataSource,
    private val localTransactionDataSource: LocalTransactionDataSource
) : WalletRepository {


    override fun getBalance(): Flow<Result<List<Balance>>> {
        return localBalanceDataSource.getAllBalancesFlow()
            .asResultFlow()
    }


    override fun getTransactions(): Flow<Result<List<Transaction>>> {
        return localTransactionDataSource.getAllTransactionsFlow()
            .asResultFlow()
    }

    override suspend fun insertOrUpdateBalance(balance: Balance): Result<Unit> {
        return safeDBCall {
            localBalanceDataSource.insertOrUpdateBalance(balance)
        }
    }

    override suspend fun getBalanceByCode(currencyCode: String): Balance? {
        return localBalanceDataSource.getBalanceByCode(currencyCode)
    }

    override suspend fun insertTransaction(
        sourceCurrencyCode: String,
        sourceAmount: Double,
        targetCurrencyCode: String,
        targetAmount: Double
    ): Result<Unit> {
        return safeDBCall {
            localTransactionDataSource.insertTransaction(
                sourceCurrencyCode,
                sourceAmount,
                targetCurrencyCode,
                targetAmount,
            )
        }
    }
}