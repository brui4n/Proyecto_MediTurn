package com.tecsup.mediturn.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecsup.mediturn.navigation.Routes
import com.tecsup.mediturn.ui.theme.*
import com.tecsup.mediturn.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {

    val email by loginViewModel.email
    val password by loginViewModel.password
    val errorMessage by loginViewModel.errorMessage
    val loggedInUser by loginViewModel.loggedInUser

    // Si el usuario ya inició sesión, navegar al Home automáticamente
    if (loggedInUser != null) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.Home.route) {
                popUpTo(Routes.Login.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔷 Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .shadow(4.dp, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(
                    Brush.verticalGradient(colors = BackgroundGradient),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .statusBarsPadding(), // esto hace que suba hasta el borde del notch
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                MediTurnLogo()
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Bienvenido a MediTurn",
                    color = WhiteText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Tu salud, nuestra prioridad",
                    color = WhiteTransparent,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { loginViewModel.email.value = it },
                label = { Text("Correo electrónico") },
                placeholder = { Text("tu@email.com") },
                singleLine = true,
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BluePrimary,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = BluePrimary,
                    focusedLabelColor = BluePrimary,
                    unfocusedLabelColor = Color.DarkGray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { loginViewModel.password.value = it },
                label = { Text("Contraseña") },
                placeholder = { Text("********") },
                singleLine = true,
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BluePrimary,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = BluePrimary,
                    focusedLabelColor = BluePrimary,
                    unfocusedLabelColor = Color.DarkGray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar error si existe
            errorMessage?.let {
                Text(text = it, color = Color.Red, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Botón Iniciar sesión
            Button(
                onClick = { loginViewModel.login() },
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Iniciar sesión",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = { /* TODO */ }) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = BluePrimary,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Divider(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp),
                color = Color.LightGray
            )

            Text(
                text = "¿No tienes una cuenta?",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { navController.navigate(Routes.Register.route) },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenAccent),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Crear cuenta nueva",
                    color = GreenAccent,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

    }
}

