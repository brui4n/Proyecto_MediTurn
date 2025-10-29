package com.tecsup.mediturn.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.mediturn.ui.theme.BluePrimary
import androidx.compose.ui.platform.LocalContext
import com.tecsup.mediturn.data.remote.RetrofitInstance
import com.tecsup.mediturn.data.repository.AppointmentRepository
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun PaymentSummaryScreen(navController: NavController, appointmentId: Int) {
    val context = LocalContext.current
    val repo = remember { AppointmentRepository(RetrofitInstance.appointmentApi(context)) }
    var isLoading by remember { mutableStateOf(true) }
    var errorText by remember { mutableStateOf<String?>(null) }
    var uiFecha by remember { mutableStateOf("") }
    var uiHora by remember { mutableStateOf("") }
    var uiDoctor by remember { mutableStateOf("") }

    LaunchedEffect(appointmentId) {
        try {
            isLoading = true
            val a = repo.getAppointmentById(appointmentId)
            // Fecha dd/MM/yyyy
            val parts = a.slot.date.split("-")
            uiFecha = "${parts[2]}/${parts[1]}/${parts[0]}"
            // Hora 12h
            val h24 = DateTimeFormatter.ofPattern("HH:mm")
            val h12 = DateTimeFormatter.ofPattern("hh:mm a")
            uiHora = try { LocalTime.parse(a.slot.time.substring(0,5), h24).format(h12) } catch (_: Exception) { a.slot.time }
            uiDoctor = a.doctor.name
            errorText = null
        } catch (e: Exception) {
            errorText = e.message
        } finally {
            isLoading = false
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            "Cita confirmada ✅",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            if (errorText == null) "Tu cita ha sido reservada exitosamente." else "No se pudo cargar el detalle: $errorText",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Detalles de la cita", fontWeight = FontWeight.Bold, color = BluePrimary)
                Spacer(modifier = Modifier.height(8.dp))
                if (isLoading) {
                    Text("Cargando...")
                } else {
                    Text("📅 Fecha: $uiFecha")
                    Text("⏰ Hora: $uiHora")
                    Text("👩‍⚕️ Médico: $uiDoctor")
                    Text("💳 Pago: S/ 150.00")
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
        ) {
            Text("Volver al inicio", color = Color.White)
        }
    }
}
