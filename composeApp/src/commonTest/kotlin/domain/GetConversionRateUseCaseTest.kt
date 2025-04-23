package org.julianvelandia.raya.domain

import kotlin.test.*
import kotlinx.coroutines.test.runTest

class GetConversionRateUseCaseTest {

    private lateinit var walletRepository: FakeWalletRepository
    private lateinit var coinRepository: CoinRepository
    private lateinit var useCase: GetConversionRateUseCase

    @BeforeTest
    fun setup() {
        walletRepository = FakeWalletRepository(mutableMapOf("usd" to Balance("","usd", 10.0)))
        coinRepository = object : CoinRepository {
            override suspend fun getCoinMarkets(coinIds: List<String>, vsCurrency: String): Result<List<CoinMarketData>> {
                return Result.success(emptyList())
            }

            override suspend fun getFiatExchangeRate(baseCode: String, targetCode: String): Result<Double> {
                return Result.success(1.0)
            }
        }
        useCase = GetConversionRateUseCase(coinRepository, walletRepository)
    }

    @Test
    fun `returns failure when not enough funds`() = runTest {
        val result = useCase("usd", "eur", 20.0)

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertIs<ConversionError.NotEnoughFunds>(exception)
        assertEquals("usd", exception.originCurrency)
    }

    @Test
    fun `returns 1_0 when origin and dest are the same currency`() = runTest {
        val result = useCase("usd", "usd", 5.0)

        assertTrue(result.isSuccess)
        assertEquals(1.0, result.getOrNull())
    }

    @Test
    fun `fiat to fiat returns correct exchange rate`() = runTest {
        val walletRepository = FakeWalletRepository(
            mutableMapOf("usd" to balanceDummy)
        )

        val coinRepository = object : CoinRepository {
            override suspend fun getCoinMarkets(coinIds: List<String>, vsCurrency: String): Result<List<CoinMarketData>> {
                return Result.success(emptyList())
            }

            override suspend fun getFiatExchangeRate(baseCode: String, targetCode: String): Result<Double> {
                if (baseCode == "usd" && targetCode == "ars") {
                    return Result.success(0.91)
                }
                return Result.failure(Exception("Unexpected currency"))
            }
        }

        val useCase = GetConversionRateUseCase(coinRepository, walletRepository)

        val result = useCase("usd", "ars", 50.0)

        assertTrue(result.isSuccess)
        assertEquals(0.91, result.getOrNull())
    }

    @Test
    fun `fiat to crypto returns inverted exchange rate`() = runTest {
        val walletRepository = FakeWalletRepository(
            mutableMapOf("usd" to Balance("", "usd", 1000.0))
        )

        val coinRepository = object : CoinRepository {
            override suspend fun getCoinMarkets(coinIds: List<String>, vsCurrency: String): Result<List<CoinMarketData>> {
                return Result.success(
                    listOf(
                        CoinMarketData(
                            id = "bitcoin",
                            symbol = "btc",
                            name = "Bitcoin",
                            imageUrl = "",
                            currentPrice = 100000.0,
                            marketCap = null,
                            marketCapRank = null
                        )
                    )
                )
            }

            override suspend fun getFiatExchangeRate(baseCode: String, targetCode: String): Result<Double> {
                return Result.failure(Exception("Not needed"))
            }
        }

        val useCase = GetConversionRateUseCase(coinRepository, walletRepository)

        val result = useCase("usd", "btc", 500.0)

        assertTrue(result.isSuccess)
        assertEquals(1 / 100000.0, result.getOrNull())
    }

    @Test
    fun `crypto to fiat returns correct market price`() = runTest {
        val walletRepository = FakeWalletRepository(
            mutableMapOf("btc" to Balance("", "btc", 1.0))
        )

        val coinRepository = object : CoinRepository {
            override suspend fun getCoinMarkets(coinIds: List<String>, vsCurrency: String): Result<List<CoinMarketData>> {
                return Result.success(
                    listOf(
                        CoinMarketData(
                            id = "bitcoin",
                            symbol = "btc",
                            name = "Bitcoin",
                            imageUrl = "",
                            currentPrice = 100000.0,
                            marketCap = null,
                            marketCapRank = null
                        )
                    )
                )
            }

            override suspend fun getFiatExchangeRate(baseCode: String, targetCode: String): Result<Double> {
                return Result.failure(Exception("Not needed"))
            }
        }

        val useCase = GetConversionRateUseCase(coinRepository, walletRepository)

        val result = useCase("btc", "usd", 0.5)

        assertTrue(result.isSuccess)
        assertEquals(100000.0, result.getOrNull())
    }

    @Test
    fun `crypto to crypto returns correct market price`() = runTest {
        val walletRepository = FakeWalletRepository(
            mutableMapOf("btc" to Balance("", "btc", 1.0))
        )

        val coinRepository = object : CoinRepository {
            override suspend fun getCoinMarkets(coinIds: List<String>, vsCurrency: String): Result<List<CoinMarketData>> {
                return Result.success(
                    listOf(
                        CoinMarketData(
                            id = "bitcoin",
                            symbol = "btc",
                            name = "Bitcoin",
                            imageUrl = "",
                            currentPrice = 50.0,
                            marketCap = null,
                            marketCapRank = null
                        )
                    )
                )
            }

            override suspend fun getFiatExchangeRate(baseCode: String, targetCode: String): Result<Double> {
                return Result.failure(Exception("Not needed"))
            }
        }

        val useCase = GetConversionRateUseCase(coinRepository, walletRepository)

        val result = useCase("btc", "eth", 1.0)

        assertTrue(result.isSuccess)
        assertEquals(50.0, result.getOrNull())
    }

    @Test
    fun `returns UnknownCurrency when crypto has no api id`() = runTest {
        val walletRepository = FakeWalletRepository(
            mutableMapOf("usd" to Balance("", "usd", 1000.0))
        )

        val coinRepository = object : CoinRepository {
            override suspend fun getCoinMarkets(coinIds: List<String>, vsCurrency: String): Result<List<CoinMarketData>> {
                return Result.failure(Exception("Should not be called"))
            }

            override suspend fun getFiatExchangeRate(baseCode: String, targetCode: String): Result<Double> {
                return Result.failure(Exception("Should not be called"))
            }
        }

        val useCase = GetConversionRateUseCase(coinRepository, walletRepository)

        val result = useCase("usd", "xyz", 100.0)

        assertTrue(result.isFailure)
        assertIs<ConversionError.UnsupportedConversion>(result.exceptionOrNull())
    }







}
