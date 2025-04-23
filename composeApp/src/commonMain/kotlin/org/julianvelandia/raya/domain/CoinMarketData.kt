package org.julianvelandia.raya.domain

data class CoinMarketData(
    val id: String,
    val symbol: String,
    val name: String,
    val imageUrl: String,
    val currentPrice: Double?,
    val marketCap: Long?,
    val marketCapRank: Int?
)