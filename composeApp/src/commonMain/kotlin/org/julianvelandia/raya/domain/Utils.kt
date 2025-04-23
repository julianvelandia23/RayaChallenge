package org.julianvelandia.raya.domain

val validCryptoInputs = setOf("bitcoin", "ethereum", "btc", "eth")
val validFiatInputs = setOf("usd", "ars")
val inputToApiIdMap = mapOf(
    "btc" to "bitcoin",
    "eth" to "ethereum"
)

val supportedCurrencies = setOf("usd", "ars")

fun String.toCryptoApiId(): String? = inputToApiIdMap[this.lowercase()]
fun String.isCrypto() = validCryptoInputs.contains(this.lowercase())
fun String.isFiat() = validFiatInputs.contains(this.lowercase())

fun parseAmount(input: String): Double? {
    return input.replace(",", "").toDoubleOrNull()
}