package org.julianvelandia.raya.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRateResponseDto(
    val result: String,
    @SerialName("base_code")
    val baseCode: String,
    @SerialName("target_code")
    val targetCode: String,
    @SerialName("conversion_rate")
    val conversionRate: Double?,
)