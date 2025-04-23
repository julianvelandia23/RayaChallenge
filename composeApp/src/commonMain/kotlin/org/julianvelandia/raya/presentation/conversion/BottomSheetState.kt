package org.julianvelandia.raya.presentation.conversion

sealed class BottomSheetState {
    data object Hidden : BottomSheetState()
    data class ShowingConfirmation(
        val amountFrom: String, val currencyFrom: String,
        val amountTo: String, val currencyTo: String, val rate: String
    ) : BottomSheetState()
    data class ShowingResult(val success: Boolean) : BottomSheetState()
}