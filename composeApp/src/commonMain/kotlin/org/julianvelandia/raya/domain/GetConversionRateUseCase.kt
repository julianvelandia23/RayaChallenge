package org.julianvelandia.raya.domain


class GetConversionRateUseCase(
    private val coinRepository: CoinRepository,
    private val walletRepository: WalletRepository
) {

    suspend operator fun invoke(origin: String, dest: String, amount: Double): Result<Double> {
        val originLower = origin.lowercase()
        val destLower = dest.lowercase()

        val originBalanceEntity = walletRepository.getBalanceByCode(origin)

        if ((originBalanceEntity?.amount ?: 0.0) < amount) {
            return Result.failure(ConversionError.NotEnoughFunds(origin))
        }

        if (originLower == destLower) return Result.success(1.0)

        return try {
            when {
                originLower.isFiat() && destLower.isFiat() -> handleFiatToFiat(originLower, destLower)
                originLower.isCrypto() && destLower.isFiat() -> handleCryptoToFiat(originLower, destLower)
                originLower.isFiat() && destLower.isCrypto() -> handleFiatToCrypto(originLower, destLower)
                originLower.isCrypto() && destLower.isCrypto() -> handleCryptoToCrypto(originLower, destLower)
                else -> Result.failure(ConversionError.UnsupportedConversion(origin, dest))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun handleFiatToFiat(origin: String, dest: String): Result<Double> {
        return if (origin in supportedCurrencies && dest in supportedCurrencies) {
            coinRepository.getFiatExchangeRate(origin, dest)
        } else {
            Result.failure(ConversionError.UnsupportedConversion(origin, dest))
        }
    }

    private suspend fun handleCryptoToFiat(crypto: String, fiat: String): Result<Double> {
        val apiId = crypto.toCryptoApiId()
            ?: return Result.failure(ConversionError.UnknownCurrency(crypto))
        return getCryptoPriceVsCurrency(apiId, fiat)
    }

    private suspend fun handleFiatToCrypto(fiat: String, crypto: String): Result<Double> {
        val apiId = crypto.toCryptoApiId()
            ?: return Result.failure(ConversionError.UnknownCurrency(crypto))

        return getCryptoPriceVsCurrency(apiId, fiat).mapCatching { price ->
            if (price == 0.0) throw ConversionError.ZeroPrice(apiId)
            1.0 / price
        }
    }

    private suspend fun handleCryptoToCrypto(origin: String, dest: String): Result<Double> {
        val originId = origin.toCryptoApiId()
            ?: return Result.failure(ConversionError.UnknownCurrency(origin))
        dest.toCryptoApiId()
            ?: return Result.failure(ConversionError.UnknownCurrency(dest))
        return getCryptoPriceVsCurrency(originId, dest)
    }

    private suspend fun getCryptoPriceVsCurrency(
        cryptoApiId: String,
        vsCurrencyCode: String
    ): Result<Double> {
        return coinRepository.getCoinMarkets(
            coinIds = listOf(cryptoApiId),
            vsCurrency = vsCurrencyCode
        ).mapCatching { marketDataList ->
            val coinData = marketDataList.firstOrNull { it.id == cryptoApiId }
            coinData?.currentPrice
                ?: throw ConversionError.PriceNotFound(cryptoApiId, vsCurrencyCode)
        }.onFailure {
            throw it
        }
    }
}
