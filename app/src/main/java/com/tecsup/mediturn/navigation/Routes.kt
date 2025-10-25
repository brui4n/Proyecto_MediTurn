package com.tecsup.mediturn.navigation

/**
 * Archivo: Routes.kt
 * Descripción: Contiene las rutas base para la navegación de la app MediTurn.
 *              Cada objeto representa una pantalla única dentro del NavHost.
 *
 * Proyecto: MediTurn
 */
sealed class Routes (val route: String){
    object Splash : Routes("splash")
    object Login : Routes("login")
    object Register: Routes("register")
    object Home : Routes("home")
    object Citas : Routes("citas")
    object Perfil : Routes("perfil")
}