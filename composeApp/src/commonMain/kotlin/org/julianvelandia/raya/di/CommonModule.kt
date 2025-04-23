package org.julianvelandia.raya.di

import db.WalletDatabase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.julianvelandia.raya.createDatabase
import org.julianvelandia.raya.data.CoinRemoteDataSource
import org.julianvelandia.raya.data.CoinRepositoryImpl
import org.julianvelandia.raya.data.LocalBalanceDataSource
import org.julianvelandia.raya.data.LocalTransactionDataSource
import org.julianvelandia.raya.data.WalletRepositoryImpl
import org.julianvelandia.raya.domain.CalculateResultUseCase
import org.julianvelandia.raya.domain.CoinRepository
import org.julianvelandia.raya.domain.GetConversionRateUseCase
import org.julianvelandia.raya.domain.PerformConversionUseCase
import org.julianvelandia.raya.domain.WalletRepository
import org.julianvelandia.raya.getKtorEngine
import org.julianvelandia.raya.presentation.WalletViewModel
import org.julianvelandia.raya.presentation.conversion.ConversionViewModel
import org.koin.core.module.Module
import org.koin.dsl.module


val appModule: Module = module {
    single { CoinRemoteDataSource(get()) }
    single { GetConversionRateUseCase(get(), get()) }
    single { PerformConversionUseCase(get()) }
    single { CalculateResultUseCase(get()) }
    single<WalletRepository> { WalletRepositoryImpl(get(), get()) }
    single<CoinRepository> { CoinRepositoryImpl(get()) }
    factory { WalletViewModel(get()) }
    factory { ConversionViewModel(get(), get(), get()) }
}

val networkModule = module {
    single {
        HttpClient(getKtorEngine()) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO
            }
        }
    }

}

val databaseModule = module {
    single<WalletDatabase> { createDatabase(get()) }
    single { get<WalletDatabase>().balanceQueries }
    single { LocalBalanceDataSource(get()) }
    single { get<WalletDatabase>().transactionQueries }
    single { LocalTransactionDataSource(get()) }
    single(createdAtStart = true) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO + exceptionHandler)
        scope.launch {
            try {
                val dataSource = get<LocalBalanceDataSource>()
                dataSource.initializeDefaultBalancesIfNeeded()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}



