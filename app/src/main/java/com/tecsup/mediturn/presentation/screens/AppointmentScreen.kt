package com.tecsup.mediturn.presentation.screens

import SlotViewModel
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecsup.mediturn.ui.theme.BluePrimary
import com.tecsup.mediturn.data.model.Slot
import androidx.compose.foundation.layout.FlowRow
import com.tecsup.mediturn.navigation.Routes
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentScreen(
    navController: NavController,
    doctorId: Int,
    slotViewModel: SlotViewModel = viewModel()
) {
    // UI state
    var selectedDate by remember { mutableStateOf("") }
    var selectedSlot by remember { mutableStateOf<Slot?>(null) }
    var motivo by remember { mutableStateOf("") }
    var selectedConsultationType by remember { mutableStateOf("PRESENCIAL") }

    // ViewModel state
    val slotsState by slotViewModel.slots.collectAsState()
    val isLoading by slotViewModel.isLoading.collectAsState()

    Log.d("DEBUG_SLOT", "slotsState size=${slotsState.size}")

    // Cargar slots desde backend
    LaunchedEffect(doctorId) {
        slotViewModel.loadSlots(doctorId)
    }

    // Extraer fechas únicas con slots disponibles del doctor
    val availableDates = remember(slotsState) {
        val todayStr = try { LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) } catch (e: Exception) { null }
        slotsState
            .filter { it.available }
            .map { it.date }
            .distinct()
            .filter { d ->
                if (todayStr == null) true else try { LocalDate.parse(d) >= LocalDate.parse(todayStr) } catch (e: Exception) { true }
            }
            .sorted()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // Encabezado
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 40.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                "Agendar cita",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Tipo de consulta
        Text("Tipo de consulta *", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            listOf("PRESENCIAL", "TELECONSULTA").forEach { type ->
                OutlinedButton(
                    onClick = { selectedConsultationType = type },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (selectedConsultationType == type) BluePrimary else Color.White,
                        contentColor = if (selectedConsultationType == type) Color.White else Color.Black
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(if (type == "PRESENCIAL") "Presencial" else "Teleconsulta")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Selección de fecha
        Text("Fecha de la cita *", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
        var expanded by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedDate.let { if (it.isEmpty()) "" else formatDateDisplay(it) },
                onValueChange = {},
                placeholder = { Text("Selecciona una fecha", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                    }
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                if (availableDates.isEmpty() && !isLoading) {
                    DropdownMenuItem(
                        text = { Text("No hay fechas disponibles", color = MaterialTheme.colorScheme.onSurface) },
                        onClick = { expanded = false }
                    )
                } else {
                    availableDates.forEach { fecha ->
                        DropdownMenuItem(
                            text = { Text(formatDateDisplay(fecha), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                selectedDate = fecha
                                selectedSlot = null
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Horarios filtrados según fecha seleccionada
        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = BluePrimary)
            }
        } else if (selectedDate.isEmpty()) {
            Text("Selecciona una fecha para ver los horarios", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        } else {

            Log.d("DEBUG_SLOT", "selectedDate=$selectedDate")
            slotsState.forEach { Log.d("DEBUG_SLOT", "slot.date=${it.date}") }

            val slotsForDate = slotsState.filter { slot ->
                slot.date.trim() == selectedDate.trim()
            }

            Text("Hora de la cita *", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(8.dp))

            if (slotsForDate.isEmpty()) {
                Text("No hay horarios disponibles", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    slotsForDate.forEach { slot ->
                        val enabled = slot.available
                        OutlinedButton(
                            onClick = { if (enabled) selectedSlot = slot },
                            enabled = true,
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = when {
                                    selectedSlot == slot -> BluePrimary
                                    !enabled -> Color(0xFFFFCDD2)
                                    else -> MaterialTheme.colorScheme.surface
                                },
                                contentColor = when {
                                    selectedSlot == slot -> Color.White
                                    !enabled -> Color.DarkGray
                                    else -> MaterialTheme.colorScheme.onSurface
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(slot.time.take(5)) // Solo la hora
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Motivo
        Text("Motivo de la consulta *", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
        OutlinedTextField(
            value = motivo,
            onValueChange = { motivo = it },
            placeholder = { Text("Describe brevemente el motivo de tu consulta...", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Info importante
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("ℹ️ Importante", fontWeight = FontWeight.SemiBold, color = BluePrimary)
                Text(
                    "Recuerda llegar 10 minutos antes de tu cita. Recibirás una confirmación por email y SMS.",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Botón confirmar
        Button(
            onClick = {
                selectedSlot?.let { slot ->
                    navController.navigate("${Routes.Payment.route}/${doctorId}/${slot.id}/${selectedConsultationType}")
                }
            },
            enabled = selectedSlot != null && motivo.isNotBlank(),
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

/** Helper: convierte "yyyy-MM-dd" a "01 Nov 2025" */
@RequiresApi(Build.VERSION_CODES.O)
private fun formatDateDisplay(iso: String): String {
    return try {
        val dt = LocalDate.parse(iso)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        dt.format(formatter)
    } catch (e: Exception) {
        iso
    }
}