package org.julianvelandia.raya.domain


class FakeWalletRepository(
    private val balances: MutableMap<String, Balance?> = mutableMapOf()
) : WalletRepository {

    override suspend fun getBalanceByCode(currencyCode: String): Balance? {
        return balances[currencyCode]
    }

    override fun getBalance() = throw NotImplementedError()
    override fun getTransactions() = throw NotImplementedError()

    override suspend fun insertOrUpdateBalance(balance: Balance): Result<Unit> {
        balances[balance.currencyCode] = balance
        return Result.success(Unit)
    }

    override suspend fun insertTransaction(
        sourceCurrencyCode: String,
        sourceAmount: Double,
        targetCurrencyCode: String,
        targetAmount: Double
    ): Result<Unit> {
        return Result.success(Unit)
    }
}

