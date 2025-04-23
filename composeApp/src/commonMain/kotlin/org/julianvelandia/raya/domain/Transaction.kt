package org.julianvelandia.raya.domain

data class Transaction(
    val uuid: String,
    val sourceCurrencyCode: String,
    val sourceAmount: Double,
    val targetCurrencyCode: String,
    val targetAmount: Double,
    val timestamp: Long
)