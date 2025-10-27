package com.tecsup.mediturn.presentation.components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.DateRange

import androidx.compose.material.icons.filled.Person

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.tecsup.mediturn.navigation.Routes

@Composable
fun BottomBar(
    navController: NavController,
    currentRoute: String,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.White
    ) {
        NavigationBarItem(
            selected = currentRoute == Routes.Home.route,
            onClick = { navController.navigate(Routes.Home.route) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1E88E5),
                selectedTextColor = Color(0xFF1E88E5),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == Routes.Citas.route,
            onClick = { navController.navigate(Routes.Citas.route) },
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Citas") },
            label = { Text("Citas") }
        )
        NavigationBarItem(
            selected = currentRoute == Routes.Perfil.route,
            onClick = { navController.navigate(Routes.Perfil.route) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }
}

