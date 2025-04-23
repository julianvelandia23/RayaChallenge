package org.julianvelandia.raya.domain

data class Balance(
    val flag: String,
    val currencyCode: String,
    val amount: Double
)