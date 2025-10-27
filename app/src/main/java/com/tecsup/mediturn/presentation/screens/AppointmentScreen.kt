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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.mediturn.ui.theme.BluePrimary
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ArrowDropDown

@Composable
fun AppointmentScreen(navController: NavController) {
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // 🔹 Encabezado
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 40.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text("Agendar cita", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 Fecha
        // 🔹 Fecha
        // 🔹 Fecha
        Text("Fecha de la cita *", fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)

        var expanded by remember { mutableStateOf(false) }
        val fechasDisponibles = listOf(
            "01 Nov 2025", "03 Nov 2025", "05 Nov 2025",
            "07 Nov 2025", "10 Nov 2025", "12 Nov 2025",
            "15 Nov 2025", "17 Nov 2025", "20 Nov 2025"
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedDate,
                onValueChange = {},
                placeholder = { Text("Selecciona una fecha") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Desplegar lista"
                        )
                    }
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
            ) {
                fechasDisponibles.forEach { fecha ->
                    DropdownMenuItem(
                        text = { Text(fecha) },
                        onClick = {
                            selectedDate = fecha
                            expanded = false
                        }
                    )
                }
            }
        }



        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 Hora
        Text("Hora de la cita *", fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
        val horas = listOf("09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
            horas.forEach { hora ->
                OutlinedButton(
                    onClick = { selectedTime = hora },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (selectedTime == hora) BluePrimary else Color.White,
                        contentColor = if (selectedTime == hora) Color.White else Color.Black
                    ),
                    shape = RoundedCornerShape(50)
                ) { Text(hora) }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 Motivo
        Text("Motivo de la consulta *", fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
        OutlinedTextField(
            value = motivo,
            onValueChange = { motivo = it },
            placeholder = { Text("Describe brevemente el motivo de tu consulta...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 Mensaje importante
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F0FE)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("ℹ️ Importante", fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold, color = BluePrimary)
                Text(
                    "Recuerda llegar 10 minutos antes de tu cita. Recibirás una confirmación por email y SMS.",
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 🔹 Botón confirmar cita
        Button(
            onClick = { navController.navigate("payment")  },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
        ) {
            Text("Confirmar cita", color = Color.White)
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}
