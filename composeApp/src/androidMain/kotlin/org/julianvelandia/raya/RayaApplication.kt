package org.julianvelandia.raya

import android.app.Application
import org.julianvelandia.raya.di.initKoinAndroid

class RayaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoinAndroid(this)
    }

}