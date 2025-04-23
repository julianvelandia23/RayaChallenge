package org.julianvelandia.raya.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : Screen("home", "Wallet", Icons.Default.Home)
    data object Conversion : Screen("conversion", "Transactions", Icons.Default.Add)
}



