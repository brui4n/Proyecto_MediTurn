package com.tecsup.mediturn.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.tecsup.mediturn.data.remote.RetrofitInstance
import com.tecsup.mediturn.data.repository.AppointmentRepository
import com.tecsup.mediturn.data.remote.dto.AppointmentCreateRequest
import com.tecsup.mediturn.data.session.SessionManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import com.tecsup.mediturn.ui.theme.BluePrimary
import com.tecsup.mediturn.navigation.Routes

@Composable
fun PaymentScreen(navController: NavController, doctorId: Int, slotId: Int, type: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sessionManager = remember { SessionManager(context) }
    val repository = remember { AppointmentRepository(RetrofitInstance.appointmentApi(context)) }
    var isSubmitting by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }
    var cardName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // 🔹 Encabezado con degradado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(BluePrimary, Color(0xFF4CAF50))
                    )
                )
                .padding(vertical = 50.dp, horizontal = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
                Text(
                    "Pago de cita",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        // 🔹 Formulario principal
        Column(modifier = Modifier.padding(20.dp)) {
            val headerColor = Color.White
            Text(
                "Ingresa los datos de tu tarjeta",
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = headerColor
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🔸 Tarjeta visual
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                colors = CardDefaults.cardColors(containerColor = BluePrimary.copy(alpha = 0.9f)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .height(150.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("MediTurn", color = Color.White.copy(alpha = 0.9f))
                    Text(
                        if (cardNumber.isEmpty()) "**** **** **** ****" else cardNumber,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (cardName.isEmpty()) "NOMBRE APELLIDO" else cardName.uppercase(),
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        Text(
                            if (expiryDate.isEmpty()) "MM/AA" else expiryDate,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // 🔸 Campos de entrada
            OutlinedTextField(
                value = cardName,
                onValueChange = { cardName = it },
                label = { Text("Nombre en la tarjeta") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                label = { Text("Número de tarjeta") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = { expiryDate = it },
                    label = { Text("MM/AA") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { cvv = it },
                    label = { Text("CVV") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🔹 Resumen del pago
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Resumen del pago", fontWeight = FontWeight.Bold, color = BluePrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Consulta médica general", color = Color.DarkGray)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Costo:", color = Color.Gray)
                        Text("S/ 150.00", fontWeight = FontWeight.SemiBold, color = BluePrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 🔹 Botón de confirmación
            Button(
                onClick = {
                    if (isSubmitting) return@Button
                    scope.launch {
                        isSubmitting = true
                        errorText = null
                        try {
                            val user = sessionManager.userSession.first()
                            val req = AppointmentCreateRequest(
                                doctor = doctorId,
                                patient = user?.id ?: 0,
                                slot = slotId,
                                consultation_type = type
                            )
                            val created = repository.createAppointment(req)
                            navController.navigate("${Routes.PaymentSummary.route}/${created.id}")
                        } catch (e: Exception) {
                            errorText = e.message
                        } finally {
                            isSubmitting = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text(
                    if (isSubmitting) "Guardando..." else "Confirmar pago",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            if (errorText != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Error: $errorText", color = Color(0xFFB00020))
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}
