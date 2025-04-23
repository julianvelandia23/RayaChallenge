package org.julianvelandia.raya.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home"){

    }
}