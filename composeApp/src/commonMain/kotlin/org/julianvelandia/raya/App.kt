package org.julianvelandia.raya

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.julianvelandia.raya.presentation.MainScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        MainScreen(navController)
    }
}