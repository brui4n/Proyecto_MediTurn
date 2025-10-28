package com.tecsup.mediturn.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tecsup.mediturn.navigation.Routes
import com.tecsup.mediturn.ui.theme.*
import com.tecsup.mediturn.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // Usamos los estados del ViewModel
    val name by viewModel.name
    val email by viewModel.email
    val phone by viewModel.phone
    val gender by viewModel.gender
    val age by viewModel.age
    val password by viewModel.password
    val confirmPassword by viewModel.confirmPassword

    val errorMessage by viewModel.errorMessage
    val successMessage by viewModel.successMessage
    val isLoading by viewModel.isLoading
    val isRegistered by viewModel.isRegistered

    // Scrollable Column
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Encabezado (igual que antes)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(colors = listOf(GreenAccent, GreenDark)),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(horizontal = 24.dp, vertical = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Text(
                    text = "Crear cuenta",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Únete a MediTurn y gestiona tu salud",
                    color = WhiteTransparent,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Formulario
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Registro", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = BluePrimary)
            Text(
                text = "Completa los siguientes datos",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            val fieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenAccent,
                focusedLabelColor = GreenAccent,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                cursorColor = GreenAccent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.DarkGray,
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray
            )

            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.name.value = it },
                label = { Text("Nombre completo") },
                placeholder = { Text("Juan Pérez García") },
                singleLine = true,
                shape = RoundedCornerShape(50),
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.email.value = it },
                label = { Text("Correo electrónico") },
                placeholder = { Text("tu@email.com") },
                singleLine = true,
                shape = RoundedCornerShape(50),
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { viewModel.phone.value = it },
                label = { Text("Teléfono") },
                placeholder = { Text("987654321") },
                singleLine = true,
                shape = RoundedCornerShape(50),
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Género", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Masculino", "Femenino", "Otro").forEach { option ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = gender == option,
                            onClick = { viewModel.gender.value = option },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = GreenAccent,
                                unselectedColor = Color.Gray
                            )
                        )
                        Text(option, color = Color.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.password.value = it },
                label = { Text("Contraseña") },
                singleLine = true,
                shape = RoundedCornerShape(50),
                visualTransformation = PasswordVisualTransformation(),
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { viewModel.confirmPassword.value = it },
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                shape = RoundedCornerShape(50),
                visualTransformation = PasswordVisualTransformation(),
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = age,
                onValueChange = { viewModel.age.value = it },
                label = { Text("Edad") },
                placeholder = { Text("25") },
                singleLine = true,
                shape = RoundedCornerShape(50),
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mostrar mensajes de error o éxito
            errorMessage?.let {
                Text(it, color = Color.Red, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }
            successMessage?.let {
                Text(it, color = Color.Green, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = { viewModel.register() },
                colors = ButtonDefaults.buttonColors(containerColor = GreenAccent),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        "Crear cuenta",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate(Routes.Login.route) }) {
                Text("¿Ya tienes una cuenta? Iniciar sesión", color = BluePrimary, fontSize = 14.sp)
            }
        }
    }

    // Navegar al login si se registró exitosamente
    if (isRegistered) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.Login.route) {
                popUpTo(Routes.Register.route) { inclusive = true }
            }
        }
    }
}