package com.tecsup.mediturn.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.mediturn.navigation.Routes

/**
 * Pantalla Login: permite al usuario iniciar sesión en MediTurn.
 */
@Composable
fun LoginScreen(navController: NavController) {
    Box(
        modifier= Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {

        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Pantalla de login")
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
                onClick = { navController.navigate(Routes.Home.route) },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Iniciar sesion")
            }

            Spacer(modifier=Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Salir")
            }
        }

    }
}