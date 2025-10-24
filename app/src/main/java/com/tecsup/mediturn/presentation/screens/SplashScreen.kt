package com.tecsup.mediturn.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tecsup.mediturn.navigation.Routes

/**
 * Pantalla Splash: muestra el logo o nombre de MediTurn mientras carga la app.
 * Puede usarse para inicializar datos o redirigir al usuario según su sesión.
 */
@Composable
fun SplashScreen(navController: NavController) {
    Box(
        modifier=Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "MediTurn",
                fontSize = 32.sp
            )
            Spacer(modifier=Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Routes.Register.route) },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Registrarse")
            }

            Spacer(modifier=Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Routes.Login.route) },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Iniciar sesion")
            }
        }
    }
}

