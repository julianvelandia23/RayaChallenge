package org.julianvelandia.raya.domain

class CalculateResultUseCase(
    private val walletRepository: WalletRepository
) {

    suspend operator fun invoke(
        amountText: String,
        rate: Double?,
        origin: String
    ): Result<Double?> {
        val originBalanceEntity = walletRepository.getBalanceByCode(origin)
        val amount = parseAmount(amountText)
        val calculated = if (amount != null && rate != null && amount > 0.0) {
            amount * rate
        } else {
            null
        }
        return when {
            (originBalanceEntity?.amount ?: 0.0) < (amount
                ?: 0.0) -> Result.failure(ConversionError.NotEnoughFunds(origin))
            else -> Result.success(calculated)
        }

    }
}