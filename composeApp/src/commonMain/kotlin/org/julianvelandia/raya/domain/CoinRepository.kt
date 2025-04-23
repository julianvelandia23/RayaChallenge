package org.julianvelandia.raya.domain


interface CoinRepository {
    suspend fun getCoinMarkets(
        coinIds: List<String>,
        vsCurrency: String
    ): Result<List<CoinMarketData>>

    suspend fun getFiatExchangeRate(
        baseCode: String,
        targetCode: String
    ): Result<Double>
}