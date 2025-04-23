package org.julianvelandia.raya

import org.koin.dsl.module

val iosPlatformModule = module {
    single<DatabaseDriverFactory> {
        DatabaseDriverFactory()
    }
}