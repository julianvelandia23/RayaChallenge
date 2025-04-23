package org.julianvelandia.raya

import java.text.DecimalFormat
import java.util.Locale

actual fun formatDecimalNumber(
    value: Double,
    minFractionDigits: Int,
    maxFractionDigits: Int
): String {
    val symbols = java.text.DecimalFormatSymbols(Locale.getDefault())

    val df = DecimalFormat().apply {
        decimalFormatSymbols = symbols
        isGroupingUsed = true
        minimumIntegerDigits = 1
        minimumFractionDigits = minFractionDigits
        maximumFractionDigits = maxFractionDigits
    }
    return df.format(value)
}