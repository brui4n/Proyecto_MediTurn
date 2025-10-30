package com.tecsup.mediturn.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecsup.mediturn.data.session.SessionManager
import com.tecsup.mediturn.navigation.Routes
import com.tecsup.mediturn.presentation.components.BottomBar
import com.tecsup.mediturn.presentation.components.DoctorCard
import com.tecsup.mediturn.ui.theme.*
import com.tecsup.mediturn.viewmodel.HomeViewModel
import com.tecsup.mediturn.viewmodel.LoginViewModel
import com.tecsup.mediturn.data.session.UserSession
import com.tecsup.mediturn.data.remote.RetrofitInstance
import com.tecsup.mediturn.data.repository.AppointmentRepository
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(navController: NavController,
               viewModel: HomeViewModel = viewModel(),
               loginViewModel: LoginViewModel = viewModel()
) {

    // estado de búsqueda desde ViewModel
    val searchQuery by viewModel.searchQuery
    val doctorsFiltered by viewModel.doctorsFiltered

    // Contexto y SessionManager
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    // Observamos la sesión guardada (DataStore)
    val userSession: UserSession? by sessionManager.userSession.collectAsState(initial = null)

    // Para que el smart casting funcione, se asigna el valor a variables locales.
    val loggedInUser = loginViewModel.loggedInUser.value
    val localUserSession = userSession

    // nombre preferido: primero el user en memoria (LoginViewModel), si no existe usamos el que venga de DataStore
    val userName = when {
        !loggedInUser?.name.isNullOrBlank() -> loggedInUser!!.name
        !localUserSession?.name.isNullOrBlank() -> localUserSession!!.name
        else -> "Usuario"
    }

    val specialties = listOf(
        "Cardiología" to "❤️",
        "Neurología" to "🧠",
        "Oftalmología" to "👁️",
        "Traumatología" to "🦴",
        "Medicina General" to "🩺",
        "Pediatría" to "👶"
    )

    // Filtro por ciudad
    var selectedCity by remember { mutableStateOf<String?>(null) } // null = todas
    val cities = remember(doctorsFiltered) {
        doctorsFiltered.mapNotNull { it.city?.takeIf { c -> c.isNotBlank() } }
            .distinct()
            .sorted()
    }

    val visibleDoctors = remember(doctorsFiltered, selectedCity) {
        if (selectedCity.isNullOrBlank()) doctorsFiltered
        else doctorsFiltered.filter { it.city.equals(selectedCity, ignoreCase = true) }
    }
    // Próxima cita del paciente
    val repo = remember { AppointmentRepository(RetrofitInstance.appointmentApi(context)) }
    var nextDoctor by remember { mutableStateOf<String?>(null) }
    var nextSpecialty by remember { mutableStateOf<String?>(null) }
    var nextDate by remember { mutableStateOf<String?>(null) }
    var nextTime by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userSession) {
        val user = userSession ?: return@LaunchedEffect
        try {
            val upcoming = repo.getAppointmentsByPatient(user.id, scope = "upcoming")
            val first = upcoming.firstOrNull()
            if (first != null) {
                nextDoctor = first.doctor.name
                nextSpecialty = first.doctor.specialty
                val parts = first.slot.date.split("-")
                nextDate = "${parts[2]}/${parts[1]}/${parts[0]}"
                val h24 = DateTimeFormatter.ofPattern("HH:mm")
                val h12 = DateTimeFormatter.ofPattern("hh:mm a")
                nextTime = try { LocalTime.parse(first.slot.time.substring(0,5), h24).format(h12) } catch (_: Exception) { first.slot.time }
            } else {
                nextDoctor = null
                nextSpecialty = null
                nextDate = null
                nextTime = null
            }
        } catch (_: Exception) {
            // silencioso para no romper home si backend falla
        }
    }

    val specialtyMap = mapOf(
        "Cardiología" to "CARDIOLOGIA",
        "Neurología" to "NEUROLOGIA",
        "Oftalmología" to "OFTALMOLOGIA",
        "Traumatología" to "TRAUMATOLOGIA",
        "Medicina General" to "MEDICINA_GENERAL",
        "Pediatría" to "PEDIATRIA"
    )


    Scaffold(
        topBar = {
            var expanded by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        Brush.horizontalGradient(listOf(BluePrimary, GreenAccent)),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .statusBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Bienvenido,", color = WhiteTransparent, fontSize = 16.sp)
                            Text(
                                "$userName 👋",
                                color = WhiteText,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // 🔽 Menú desplegable de usuario
                        Box {
                            IconButton(
                                onClick = { expanded = true },
                                modifier = Modifier
                                    .size(40.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Menú de usuario",
                                    tint = WhiteText
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Cerrar sesión", color = Color.Red) },
                                    onClick = {
                                        expanded = false
                                        loginViewModel.logout()
                                        navController.navigate(Routes.Login.route) {
                                            popUpTo(Routes.Home.route) { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo de búsqueda (igual que antes)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { viewModel.onSearchQueryChanged(it) },
                                singleLine = true,
                                textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 17.sp),
                                modifier = Modifier.weight(1f),
                                decorationBox = { innerTextField ->
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "Buscar médicos o especialidades...",
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                            fontSize = 16.sp
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                currentRoute = Routes.Home.route
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (searchQuery.isBlank()) {
                // 🔹 Filtro horizontal por ciudades (arriba)
                if (cities.isNotEmpty()) {
                    Button(
                        onClick = { navController.navigate(Routes.CityFilter.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.height(40.dp)
                    ) { Text("Filtrar por ciudad", color = Color.White) }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 🗓️ Card próxima cita
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("📅 Próxima cita", color = BluePrimary, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        if (nextDoctor != null) {
                            Text(nextDoctor!!, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            val extra = listOfNotNull(nextSpecialty, listOfNotNull(nextDate, nextTime).joinToString(" · ")).filter { it.isNotBlank() }.joinToString("  •  ")
                            if (extra.isNotBlank()) Text(extra, color = Color.Gray)
                        } else {
                            Text("Sin próximas citas", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("Agenda tu primera cita", color = Color.Gray)
                        }

                        if (nextDoctor != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { navController.navigate(Routes.Citas.route) },
                                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                                shape = RoundedCornerShape(50),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                modifier = Modifier
                                    .height(36.dp)
                                    .defaultMinSize(minWidth = 88.dp)
                                    .align(Alignment.Start)
                            ) {
                                Text("Ver", maxLines = 1)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (!selectedCity.isNullOrBlank()) {
                    // Lista de doctores por ciudad
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(visibleDoctors.size) { index ->
                            val doctor = visibleDoctors[index]
                            DoctorCard(
                                doctor = doctor,
                                onDetailClick = {
                                    navController.navigate("${Routes.DoctorDetail.route}/${doctor.id}")
                                })
                        }
                    }
                } else {
                    Text(
                        text = "Buscar por especialidad",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(specialties) { (name, icon) ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier
                                    .height(90.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        val specialtyCode = specialtyMap[name] ?: name
                                        navController.navigate("${Routes.DoctorList.route}/$specialtyCode")
                                    }
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(icon, fontSize = 26.sp)
                                    Text(name, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
                                }
                            }
                        }
                    }
                }
            } else {
                if (doctorsFiltered.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No se encontraron resultados 😕", color = Color.Gray)
                    }
                } else {
                val visibleDoctors = remember(doctorsFiltered, selectedCity) {
                    if (selectedCity.isNullOrBlank()) doctorsFiltered
                    else doctorsFiltered.filter { it.city.equals(selectedCity, ignoreCase = true) }
                }
                LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                    items(visibleDoctors.size) { index ->
                        val doctor = visibleDoctors[index]
                            DoctorCard(
                                doctor = doctor,
                                onDetailClick = {
                                    navController.navigate("${Routes.DoctorDetail.route}/${doctor.id}")
                                })
                        }
                    }
                }

            }

        }
    }
}
