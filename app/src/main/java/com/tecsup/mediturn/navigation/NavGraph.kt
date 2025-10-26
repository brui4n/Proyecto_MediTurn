package com.tecsup.mediturn.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.mediturn.data.session.SessionManager
import com.tecsup.mediturn.presentation.screens.HomeScreen
import com.tecsup.mediturn.presentation.screens.LoginScreen
import com.tecsup.mediturn.presentation.screens.RegisterScreen
import com.tecsup.mediturn.presentation.screens.SplashScreen
import com.tecsup.mediturn.viewmodel.SplashViewModel

/**
 * Archivo: NavGraph.kt
 * Descripción: Define el flujo de navegación principal de la aplicación MediTurn
 *              utilizando Navigation Compose.
 * Proyecto: MediTurn
 */

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        composable(Routes.Splash.route) {
            val context = LocalContext.current
            val sessionManager = remember { SessionManager(context) }
            val splashViewModel = remember { SplashViewModel(sessionManager) }

            SplashScreen(navController, splashViewModel)
        }
        composable(Routes.Login.route) { LoginScreen(navController) }
        composable(Routes.Register.route) { RegisterScreen(navController) }
        composable(Routes.Home.route) { HomeScreen(navController) }

    }
}