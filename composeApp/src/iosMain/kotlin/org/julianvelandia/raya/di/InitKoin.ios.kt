package org.julianvelandia.raya.di

import org.julianvelandia.raya.iosPlatformModule
import org.koin.core.context.startKoin

fun initKoinIos() {
    startKoin {
        modules(appModule, networkModule, iosPlatformModule, databaseModule)
    }
}
