package org.julianvelandia.raya.data

import db.BalanceEntity
import db.TransactionEntity
import org.julianvelandia.raya.domain.Balance
import org.julianvelandia.raya.domain.CoinMarketData
import org.julianvelandia.raya.domain.Transaction

fun CoinMarketDataDto.toDomainModel(): CoinMarketData {
    return CoinMarketData(
        id = id,
        symbol = symbol,
        name = name,
        imageUrl = image.orEmpty(),
        currentPrice = currentPrice,
        marketCap = marketCap,
        marketCapRank = marketCapRank
    )
}

fun BalanceEntity.toDomain(): Balance {
    return Balance(
        flag = this.flag,
        currencyCode = this.currencyCode,
        amount = this.amount
    )
}

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        uuid = this.uuid, // Mapea desde la entidad
        sourceCurrencyCode = this.sourceCurrencyCode,
        sourceAmount = this.sourceAmount,
        targetCurrencyCode = this.targetCurrencyCode,
        targetAmount = this.targetAmount,
        timestamp = this.transactionTimestamp
    )
}