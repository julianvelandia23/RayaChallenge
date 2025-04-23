package org.julianvelandia.raya.di

import android.app.Application
import org.julianvelandia.raya.androidPlatformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


fun initKoinAndroid(app: Application) {
    startKoin {
        androidContext(app)
        modules(appModule, networkModule, androidPlatformModule, databaseModule)
    }
}