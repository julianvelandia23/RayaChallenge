package org.julianvelandia.raya.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import db.BalanceQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.julianvelandia.raya.domain.Balance

class LocalBalanceDataSource(
    private val queries: BalanceQueries
) {

    suspend fun insertOrUpdateBalance(balance: Balance) {
        withContext(Dispatchers.IO) {
            queries.insertOrReplaceBalance(
                currencyCode = balance.currencyCode,
                flag = balance.flag,
                amount = balance.amount
            )
        }
    }

    private suspend fun insertOrUpdateBalances(balances: List<Balance>) {
        withContext(Dispatchers.IO) {
            queries.transaction {
                balances.forEach { balance ->
                    queries.insertOrReplaceBalance(
                        currencyCode = balance.currencyCode,
                        flag = balance.flag,
                        amount = balance.amount
                    )
                }
            }
        }
    }

    fun getAllBalancesFlow(): Flow<List<Balance>> {
        return queries.selectAllBalances()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entityList ->
                entityList.map { it.toDomain() }
            }
    }


    suspend fun getBalanceByCode(currencyCode: String): Balance? {
        return withContext(Dispatchers.IO) {
            queries.selectBalanceByCode(currencyCode)
                .executeAsOneOrNull()
                ?.toDomain()

        }
    }

    suspend fun initializeDefaultBalancesIfNeeded() {
        withContext(Dispatchers.IO) {
            val result = queries.hasAnyBalance().executeAsOneOrNull()
            if (result == null) {
                val defaultBalances = listOf(
                    Balance("🇦🇷", "ARS", 52000.0),
                    Balance("🇺🇸", "USD", 2000.0),
                    Balance("₿", "BTC", 0.01321),
                    Balance("Ξ", "ETH", 1.0911)
                )
                try {
                    insertOrUpdateBalances(defaultBalances)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}