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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.mediturn.navigation.Routes

/**
 * Pantalla Home: vista principal de la aplicación MediTurn.
 * Aquí se mostrarán las opciones o funcionalidades disponibles.
 */

@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {

        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Pantalla Principal (Home)")
            Spacer(modifier=Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate(Routes.Splash.route) },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Salir")
            }
        }
    }
}