package com.tecsup.mediturn.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.navigation.Routes
import com.tecsup.mediturn.ui.theme.BluePrimary
import com.tecsup.mediturn.ui.theme.GreenAccent
import com.tecsup.mediturn.util.Resource
import com.tecsup.mediturn.viewmodel.DoctorViewModel
import com.tecsup.mediturn.viewmodel.DoctorScheduleViewModel

@Composable
fun DoctorDetailScreen(
    navController: NavController,
    doctorId: Int,
    viewModel: DoctorViewModel = viewModel(),
    scheduleViewModel: DoctorScheduleViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableStateOf(0) }

    // Observamos los estados
    val doctorState by viewModel.doctorDetail.collectAsState()
    val scheduleState by scheduleViewModel.schedules.collectAsState()

    // Cargar datos al entrar
    LaunchedEffect(doctorId) {
        viewModel.loadDoctorById(doctorId)
        scheduleViewModel.loadSchedulesByDoctor(doctorId)
    }

    when (doctorState) {
        is Resource.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = BluePrimary)
            }
        }

        is Resource.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = doctorState.message ?: "Error al cargar los datos del médico",
                    color = Color.Red
                )
            }
        }

        is Resource.Success -> {
            val doctor = doctorState.data!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .background(Color(0xFFF8F9FA))
            ) {
                // 🔹 Encabezado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(BluePrimary, GreenAccent)
                            )
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(top = 40.dp, start = 16.dp)
                            .align(Alignment.TopStart)
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "Detalle del médico",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // 🔹 Card principal
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .offset(y = (-40).dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val imageUrl = if (!doctor.image.isNullOrBlank()) {
                                doctor.image
                            } else {
                                "https://cdn-icons-png.flaticon.com/512/2922/2922506.png"
                            }

                            Image(
                                painter = rememberAsyncImagePainter(imageUrl),
                                contentDescription = doctor.name,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = doctor.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = doctor.specialty,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Rating",
                                        tint = Color(0xFFFFC107)
                                    )
                                    Text(
                                        text = "${doctor.rating ?: 0.0}",
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { navController.navigate(Routes.Appointment.route) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BluePrimary,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Agendar cita")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 🔹 Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    containerColor = Color.Transparent,
                    contentColor = BluePrimary
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Información") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Horarios") }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 🔹 Contenido según pestaña
                when (selectedTab) {
                    0 -> {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            InfoCard(
                                title = "Educación",
                                description = doctor.education ?: "Educación no especificada"
                            )
                            InfoCard(
                                title = "Experiencia",
                                description = doctor.experienceDesc ?: "Experiencia no especificada"
                            )
                            InfoCard(
                                title = "Ubicación",
                                description = doctor.city ?: "Ubicación no especificada"
                            )
                        }
                    }

                    1 -> {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            when (scheduleState) {
                                is Resource.Loading -> {
                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 20.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(color = BluePrimary)
                                    }
                                }

                                is Resource.Error -> {
                                    Text(
                                        text = scheduleState.message ?: "Error al cargar horarios",
                                        color = Color.Red
                                    )
                                }

                                is Resource.Success -> {
                                    val schedules = scheduleState.data ?: emptyList()
                                    if (schedules.isEmpty()) {
                                        Text(
                                            text = "No hay horarios registrados",
                                            color = Color.Gray
                                        )
                                    } else {
                                        schedules.forEach { schedule ->
                                            ScheduleCard(
                                                day = schedule.weekday_name,
                                                hours = "${schedule.start_time} - ${schedule.end_time}"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun InfoCard(title: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = BluePrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun ScheduleCard(day: String, hours: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = day, fontWeight = FontWeight.SemiBold, color = BluePrimary)
            Text(text = hours, color = Color.Gray)
        }
    }
}