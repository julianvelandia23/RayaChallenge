package org.julianvelandia.raya.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import org.julianvelandia.raya.presentation.conversion.ConversionScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val canNavigateBack = navController.previousBackStackEntry != null
    navBackStackEntry?.destination?.route
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    if (canNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back",
                            modifier = Modifier
                                .padding(dimens8dp)
                                .clickable { navController.popBackStack() }
                        )
                    }
                },
                title = {
                    Text(
                        text = "Crypto Wallet"
                    )
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                WalletScreen(navigateTo = { route ->
                    navController.navigate(route)
                })
            }
            composable(Screen.Conversion.route) {
                ConversionScreen()
            }
        }
    }
}