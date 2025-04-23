package org.julianvelandia.raya.domain

sealed class ConversionError(message: String) : Exception(message) {
    class UnknownCurrency(currency: String) : ConversionError("Moneda no reconocida: $currency")
    class NotEnoughFunds(val originCurrency: String) : ConversionError("Fondos insuficientes para $originCurrency")
    class UnsupportedConversion(from: String, to: String) : ConversionError("Conversión no soportada: $from -> $to")
    class ZeroPrice(currency: String) : ConversionError("Precio de $currency es cero")
    class PriceNotFound(currency: String, vsCurrency: String) : ConversionError("Precio no encontrado para $currency en $vsCurrency")
    data object MountNotValid : ConversionError("Monto a convertir inválido.")
    data object ResultNotValid : ConversionError("Resultado de conversión inválido o tasa no disponible.")
}
