package org.julianvelandia.raya

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidPlatformModule = module {
    single<DatabaseDriverFactory> {
        DatabaseDriverFactory(androidContext())
    }
}