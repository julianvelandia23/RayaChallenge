package org.julianvelandia.raya.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.julianvelandia.raya.Secrets

class CoinRemoteDataSource(
    private val httpClient: HttpClient
) {

    suspend fun getCoinMarkets(
        vsCurrency: String,
        coinIds: List<String>? = null,
    ): List<CoinMarketDataDto> {
        val response = httpClient.get(COIN_MARKETS_PATH) {
            url {
                parameters.append(PARAM_VS_CURRENCY, vsCurrency)
                if (!coinIds.isNullOrEmpty()) {
                    parameters.append(PARAM_ID, coinIds.joinToString(","))
                }
                parameters.append(KEY, Secrets.COIN_GECKO_KEY)
            }
        }
        return response.body<List<CoinMarketDataDto>>()
    }

    suspend fun getFiatExchangeRate(
        baseCode: String,
        targetCode: String,
    ): ExchangeRateResponseDto {
        val apiUrl =
            "$EXCHANGE_RATE_API_BASE/${Secrets.EXCHANGE_KEY}/$EXCHANGE_RATE_PATH/$baseCode/$targetCode"
        val response = httpClient.get(apiUrl)
        return response.body<ExchangeRateResponseDto>()
    }
}

const val COIN_MARKETS_PATH = "https://api.coingecko.com/api/v3/coins/markets"
const val KEY = "x_cg_demo_api_key"
const val PARAM_VS_CURRENCY = "vs_currency"
const val PARAM_ID= "ids"

const val EXCHANGE_RATE_API_BASE = "https://v6.exchangerate-api.com/v6"
const val EXCHANGE_RATE_PATH = "pair"