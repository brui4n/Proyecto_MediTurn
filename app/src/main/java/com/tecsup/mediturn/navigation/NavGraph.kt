package com.tecsup.mediturn.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mediturn.data.repository.DoctorRepository
import com.tecsup.mediturn.data.session.SessionManager
import com.tecsup.mediturn.presentation.screens.*
import com.tecsup.mediturn.viewmodel.SplashViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {

        // Pantalla Splash
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

        // 🔹 DoctorListScreen con parámetro specialty
        composable(
            route = Routes.DoctorList.route + "/{specialty}",
            arguments = listOf(navArgument("specialty") { type = NavType.StringType })
        ) { backStackEntry ->
            val specialty = backStackEntry.arguments?.getString("specialty") ?: ""
            DoctorListScreen(navController = navController, specialty = specialty)
        }

        composable(
            route = Routes.DoctorDetail.route + "/{doctorId}",
            arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            DoctorDetailScreen(navController = navController, doctorId = doctorId)
        }



        // Otras pantallas
        composable(Routes.Appointment.route) { AppointmentScreen(navController) }
        composable(Routes.Payment.route) { PaymentScreen(navController) }
        composable(Routes.PaymentSummary.route) { PaymentSummaryScreen(navController) }
    }
}