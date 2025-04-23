package org.julianvelandia.raya

import androidx.compose.ui.window.ComposeUIViewController
import org.julianvelandia.raya.di.initKoinIos
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoinIos()
    return ComposeUIViewController {
        App()
    }
}