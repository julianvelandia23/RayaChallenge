package org.julianvelandia.raya.data

import org.julianvelandia.raya.domain.CoinMarketData
import org.julianvelandia.raya.domain.CoinRepository

class CoinRepositoryImpl(
    private val coinRemoteDataSource: CoinRemoteDataSource
) : CoinRepository {

    override suspend fun getCoinMarkets(
        coinIds: List<String>,
        vsCurrency: String
    ): Result<List<CoinMarketData>> {
        return safeCall {
            val priceResponseDto = coinRemoteDataSource.getCoinMarkets(vsCurrency, coinIds)
            val domainModelList: List<CoinMarketData> = priceResponseDto.map { it.toDomainModel() }
            domainModelList
        }
    }

    override suspend fun getFiatExchangeRate(
        baseCode: String,
        targetCode: String
    ): Result<Double> {
        return safeCall {
            coinRemoteDataSource.getFiatExchangeRate(baseCode, targetCode).conversionRate ?: 0.0
        }
    }
}