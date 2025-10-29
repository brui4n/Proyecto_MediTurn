package com.tecsup.mediturn.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.AccessTime

import androidx.navigation.NavController
import com.tecsup.mediturn.navigation.Routes
import com.tecsup.mediturn.presentation.components.BottomBar
import androidx.compose.ui.platform.LocalContext
import com.tecsup.mediturn.data.remote.RetrofitInstance
import com.tecsup.mediturn.data.repository.AppointmentRepository
import com.tecsup.mediturn.data.repository.SlotRepository
import com.tecsup.mediturn.data.session.SessionManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.tecsup.mediturn.ui.theme.BluePrimary

data class Cita(
    val appointmentId: Int,
    val doctorId: Int,
    val doctor: String,
    val especialidad: String,
    val fecha: String,
    val hora: String,
    val iniciales: String
)

@Composable
fun CitasScreen(navController: NavController) {

    var selectedTab by remember { mutableStateOf(0) } // 0 = próximas, 1 = historial
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val session = remember { SessionManager(context) }
    val repo = remember { AppointmentRepository(RetrofitInstance.appointmentApi(context)) }
    val slotRepo = remember { SlotRepository(RetrofitInstance.slotApi(context)) }

    var proximasCitas by remember { mutableStateOf<List<Cita>>(emptyList()) }
    var historialCitas by remember { mutableStateOf<List<Cita>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorText by remember { mutableStateOf<String?>(null) }
    var openCancelDialog by remember { mutableStateOf<Cita?>(null) }
    var openRescheduleFor by remember { mutableStateOf<Cita?>(null) }
    var availableSlots by remember { mutableStateOf<List<com.tecsup.mediturn.data.model.Slot>>(emptyList()) }
    var selectedDate by remember { mutableStateOf("") }
    var selectedSlotId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(true) {
        scope.launch {
            try {
                isLoading = true
                val user = session.userSession.first() ?: return@launch
                val upcoming = repo.getAppointmentsByPatient(user.id, scope = "upcoming")
                val past = repo.getAppointmentsByPatient(user.id, scope = "past")

                val hourFormatter24 = DateTimeFormatter.ofPattern("HH:mm")
                val hourFormatter12 = DateTimeFormatter.ofPattern("hh:mm a")

                fun mapToCita(a: com.tecsup.mediturn.data.model.Appointment): Cita {
                    val initials = a.doctor.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }
                        .joinToString("")
                        .take(3)
                        .uppercase()
                    val datePieces = a.slot.date.split("-")
                    val fecha = "${datePieces[2]}/${datePieces[1]}/${datePieces[0]}"
                    val hora = try { LocalTime.parse(a.slot.time.substring(0,5), hourFormatter24).format(hourFormatter12) } catch (_: Exception) { a.slot.time }
                    return Cita(
                        appointmentId = a.id,
                        doctorId = a.doctor.id,
                        doctor = a.doctor.name,
                        especialidad = a.doctor.specialty,
                        fecha = fecha,
                        hora = hora,
                        iniciales = initials
                    )
                }

                proximasCitas = upcoming.map(::mapToCita)
                historialCitas = past.map(::mapToCita)
                errorText = null
            } catch (e: Exception) {
                errorText = e.message
            } finally {
                isLoading = false
            }
        }
    }

    val citas = if (selectedTab == 0) proximasCitas else historialCitas

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                currentRoute = Routes.Citas.route
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Mis citas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                indicator = {},
                divider = {}
            ) {
                listOf("Próximas (${proximasCitas.size})", "Historial (${historialCitas.size})").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                color = if (selectedTab == index) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier
                            .background(
                                if (selectedTab == index) BluePrimary else Color.Transparent,
                                RoundedCornerShape(50)
                            )
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Lista de citas
            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BluePrimary)
                }
            } else if (errorText != null) {
                Text("Error: $errorText", color = Color(0xFFB00020))
            } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(citas) { cita ->
                        CitaCard(
                            cita = cita,
                            onCancel = { selected ->
                                // Confirmar cancelación
                                openCancelDialog = selected
                            },
                            onReschedule = { selected ->
                                openRescheduleFor = selected
                                // cargar slots disponibles del doctor
                                scope.launch {
                                    try {
                                        val slots = slotRepo.getSlotsByDoctor(selected.doctorId)
                                        availableSlots = slots.filter { it.available }
                                        selectedDate = availableSlots.firstOrNull()?.date ?: ""
                                        selectedSlotId = null
                                    } catch (e: Exception) {
                                        errorText = e.message
                                    }
                                }
                            }
                        )
                    }
                }
            }

            // Dialogo de cancelar
            openCancelDialog?.let { c ->
                AlertDialog(
                    onDismissRequest = { openCancelDialog = null },
                    confirmButton = {
                        TextButton(onClick = {
                            scope.launch {
                                try {
                                    repo.cancelAppointment(c.appointmentId)
                                    // refrescar listas
                                    val user = session.userSession.firstOrNull() ?: return@launch
                                    val upcoming = repo.getAppointmentsByPatient(user.id, scope = "upcoming")
                                    val past = repo.getAppointmentsByPatient(user.id, scope = "past")
                                    val hourFormatter24 = DateTimeFormatter.ofPattern("HH:mm")
                                    val hourFormatter12 = DateTimeFormatter.ofPattern("hh:mm a")
                                    fun mapToCitaLocal(a: com.tecsup.mediturn.data.model.Appointment): Cita {
                                        val initials = a.doctor.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }
                                            .joinToString("").take(3).uppercase()
                                        val parts = a.slot.date.split("-")
                                        val fecha = "${parts[2]}/${parts[1]}/${parts[0]}"
                                        val hora = try { LocalTime.parse(a.slot.time.substring(0,5), hourFormatter24).format(hourFormatter12) } catch (_: Exception) { a.slot.time }
                                        return Cita(a.id, a.doctor.id, a.doctor.name, a.doctor.specialty, fecha, hora, initials)
                                    }
                                    proximasCitas = upcoming.map(::mapToCitaLocal)
                                    historialCitas = past.map(::mapToCitaLocal)
                                } finally { openCancelDialog = null }
                            }
                        }) { Text("Sí, cancelar") }
                    },
                    dismissButton = { TextButton(onClick = { openCancelDialog = null }) { Text("No") } },
                    title = { Text("Cancelar cita") },
                    text = { Text("¿Deseas cancelar tu cita con ${c.doctor}?") }
                )
            }

            // Dialogo de reprogramar
            openRescheduleFor?.let { c ->
                val dates = availableSlots.map { it.date }.distinct()
                val slotsForDate = availableSlots.filter { it.date == selectedDate }
                AlertDialog(
                    onDismissRequest = { openRescheduleFor = null },
                    confirmButton = {
                        TextButton(onClick = {
                            val slotId = selectedSlotId
                            if (slotId != null) {
                                scope.launch {
                                    try {
                                        repo.rescheduleAppointment(c.appointmentId, slotId)
                                        val user = session.userSession.firstOrNull() ?: return@launch
                                        val upcoming = repo.getAppointmentsByPatient(user.id, scope = "upcoming")
                                        val past = repo.getAppointmentsByPatient(user.id, scope = "past")
                                        val hourFormatter24 = DateTimeFormatter.ofPattern("HH:mm")
                                        val hourFormatter12 = DateTimeFormatter.ofPattern("hh:mm a")
                                        fun mapToCitaLocal(a: com.tecsup.mediturn.data.model.Appointment): Cita {
                                            val initials = a.doctor.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }
                                                .joinToString("").take(3).uppercase()
                                            val parts = a.slot.date.split("-")
                                            val fecha = "${parts[2]}/${parts[1]}/${parts[0]}"
                                            val hora = try { LocalTime.parse(a.slot.time.substring(0,5), hourFormatter24).format(hourFormatter12) } catch (_: Exception) { a.slot.time }
                                            return Cita(a.id, a.doctor.id, a.doctor.name, a.doctor.specialty, fecha, hora, initials)
                                        }
                                        proximasCitas = upcoming.map(::mapToCitaLocal)
                                        historialCitas = past.map(::mapToCitaLocal)
                                    } finally { openRescheduleFor = null }
                                }
                            }
                        }) { Text("Confirmar") }
                    },
                    dismissButton = { TextButton(onClick = { openRescheduleFor = null }) { Text("Cerrar") } },
                    title = { Text("Reprogramar cita") },
                    text = {
                        Column {
                            Text("Selecciona fecha")
                            Spacer(Modifier.height(8.dp))
                            // fechas
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                dates.forEach { d ->
                                    val isSelected = d == selectedDate
                                    OutlinedButton(
                                        onClick = { selectedDate = d; selectedSlotId = null },
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = if (isSelected) BluePrimary else Color.White,
                                            contentColor = if (isSelected) Color.White else Color.Black
                                        ),
                                        shape = RoundedCornerShape(50)
                                    ) { Text(d) }
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                            Text("Selecciona hora")
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                slotsForDate.forEach { s ->
                                    val isSelected = selectedSlotId == s.id
                                    OutlinedButton(
                                        onClick = { selectedSlotId = s.id },
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = if (isSelected) BluePrimary else Color.White,
                                            contentColor = if (isSelected) Color.White else Color.Black
                                        ),
                                        shape = RoundedCornerShape(50)
                                    ) { Text(s.time.take(5)) }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun CitaCard(
    cita: Cita,
    onCancel: (Cita) -> Unit,
    onReschedule: (Cita) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(Color(0xFF2196F3), Color(0xFF42A5F5))
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        cita.iniciales,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(cita.doctor, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text(cita.especialidad, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = BluePrimary)
                Text(cita.fecha, color = MaterialTheme.colorScheme.onSurface)
                Icon(Icons.Default.AccessTime, contentDescription = null, tint = BluePrimary)
                Text(cita.hora, color = MaterialTheme.colorScheme.onSurface)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { onReschedule(cita) }) {
                    Text("Reprogramar")
                }
                OutlinedButton(onClick = { onCancel(cita) }, colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB00020))) {
                    Text("Cancelar")
                }
            }
        }
    }
}
