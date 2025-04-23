package org.julianvelandia.raya.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinMarketDataDto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String?,
    @SerialName("current_price")
    val currentPrice: Double?,
    @SerialName("market_cap")
    val marketCap: Long?,
    @SerialName("market_cap_rank")
    val marketCapRank: Int?
)