package com.tecsup.mediturn.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.mediturn.data.session.SessionManager
import com.tecsup.mediturn.presentation.screens.DoctorListScreen
import com.tecsup.mediturn.presentation.screens.HomeScreen
import com.tecsup.mediturn.presentation.screens.LoginScreen
import com.tecsup.mediturn.presentation.screens.RegisterScreen
import com.tecsup.mediturn.presentation.screens.SplashScreen
import com.tecsup.mediturn.viewmodel.SplashViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.tecsup.mediturn.repository.DoctorRepository
import com.tecsup.mediturn.presentation.screens.DoctorDetailScreen
import com.tecsup.mediturn.presentation.screens.AppointmentScreen
import com.tecsup.mediturn.presentation.screens.CitasScreen
import com.tecsup.mediturn.presentation.screens.PaymentScreen
import com.tecsup.mediturn.presentation.screens.PaymentSummaryScreen
import com.tecsup.mediturn.presentation.screens.PerfilScreen


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
        composable(Routes.Citas.route) { CitasScreen(navController) }
        composable(Routes.Perfil.route) { PerfilScreen(navController) }


        composable(
            route = Routes.DoctorList.route + "/{specialty}",
            arguments = listOf(navArgument("specialty") { type = NavType.StringType })
        ) { backStackEntry ->
            val specialty = backStackEntry.arguments?.getString("specialty") ?: ""
            DoctorListScreen(navController, specialty)
        }
        composable(
            route = "doctor_detail/{doctorId}",
            arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            val repository = DoctorRepository()
            val doctor = repository.getDoctorById(doctorId)

            doctor?.let {
                DoctorDetailScreen(navController = navController, doctor = it)
            }
        }
        composable("appointment") {
            AppointmentScreen(navController)
        }
        composable(Routes.Payment.route) { PaymentScreen(navController) }
        composable(Routes.PaymentSummary.route) { PaymentSummaryScreen(navController) }



    }
}
