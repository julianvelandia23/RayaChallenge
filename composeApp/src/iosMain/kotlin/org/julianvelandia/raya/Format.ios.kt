package org.julianvelandia.raya

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle
actual fun formatDecimalNumber(
    value: Double,
    minFractionDigits: Int,
    maxFractionDigits: Int
): String {
    val formatter = NSNumberFormatter()
    formatter.numberStyle = NSNumberFormatterDecimalStyle
    formatter.usesGroupingSeparator = true
    formatter.minimumIntegerDigits = 1u
    formatter.minimumFractionDigits = minFractionDigits.toULong()
    formatter.maximumFractionDigits = maxFractionDigits.toULong()
    val number = NSNumber(value)
    return formatter.stringFromNumber(number) ?: value.toString()
}