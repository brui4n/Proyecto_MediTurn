package com.tecsup.mediturn.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tecsup.mediturn.presentation.components.BottomBar
import com.tecsup.mediturn.navigation.Routes
import com.tecsup.mediturn.ui.theme.BluePrimary
import com.tecsup.mediturn.R

@Composable
fun PerfilScreen(navController: NavController) {
    var isEditing by remember { mutableStateOf(false) }

    // Datos de usuario (mock temporal)
    var nombre by remember { mutableStateOf("Ana García Martínez") }
    var email by remember { mutableStateOf("ana.garcia@email.com") }
    var telefono by remember { mutableStateOf("+52 55 1234 5678") }
    var fechaNacimiento by remember { mutableStateOf("15/03/1990") }

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController, currentRoute = Routes.Perfil.route)
        },
        containerColor = Color(0xFFF8F9FA)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // 🔹 Fondo degradado superior (más alto)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp) // 🔸 más grande que antes
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(BluePrimary, Color(0xFF4CAF50))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Mi perfil",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            // 🔹 Tarjeta principal (flotando encima del degradado)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = 130.dp), // 🔸 hace que la tarjeta suba sobre el degradado
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Imagen circular
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(90.dp)
                                .background(Color.Gray, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Paciente activo", color = Color.Gray, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    PerfilItem(icon = Icons.Default.Email, text = email)
                    PerfilItem(icon = Icons.Default.Call, text = telefono)
                    PerfilItem(icon = Icons.Default.Event, text = fechaNacimiento)

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { isEditing = !isEditing },
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (isEditing) "Guardar cambios" else "Editar perfil",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PerfilItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Icon(icon, contentDescription = null, tint = BluePrimary)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = Color.DarkGray)
    }
}
