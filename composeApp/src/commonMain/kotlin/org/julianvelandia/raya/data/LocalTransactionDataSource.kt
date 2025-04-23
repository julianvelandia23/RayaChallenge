package org.julianvelandia.raya.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import db.TransactionEntity
import db.TransactionQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.julianvelandia.raya.domain.Transaction
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class LocalTransactionDataSource(
    private val queries: TransactionQueries
) {

    fun getAllTransactionsFlow(): Flow<List<Transaction>> {
        return queries.selectAllTransactions()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entityList ->
                entityList.map { it.toDomain() }
            }
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun insertTransaction(
        sourceCurrencyCode: String,
        sourceAmount: Double,
        targetCurrencyCode: String,
        targetAmount: Double
    ): Result<Unit> {
        return try {
            val newUuid = Uuid.random().toString()
            val currentTimestamp = Clock.System.now().toEpochMilliseconds()

            withContext(Dispatchers.IO) {
                queries.insertTransaction(
                    uuid = newUuid,
                    sourceCurrencyCode = sourceCurrencyCode,
                    sourceAmount = sourceAmount,
                    targetCurrencyCode = targetCurrencyCode,
                    targetAmount = targetAmount,
                    transactionTimestamp = currentTimestamp
                )
            }
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

}