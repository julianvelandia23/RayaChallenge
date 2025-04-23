package org.julianvelandia.raya.presentation

import org.julianvelandia.raya.formatDecimalNumber


fun String.isValidDecimalInput(): Boolean {
    return matches(Regex("^\\d*\\.?\\d*\$"))
}

fun formatCurrency(amount: Double?, currency: String, maxFractionDigits: Int = 8): String {
    if (amount == null) return "---"
    val formattedAmount = formatDecimalNumber(amount, 0, maxFractionDigits)
    return "$formattedAmount $currency"
}

val supportCurrencies: List<String> = listOf("ARS", "USD", "BTC", "ETH")