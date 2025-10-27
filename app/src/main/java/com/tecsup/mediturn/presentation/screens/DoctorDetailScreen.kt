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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.navigation.Routes
import com.tecsup.mediturn.ui.theme.BluePrimary
import com.tecsup.mediturn.ui.theme.GreenAccent

@Composable
fun DoctorDetailScreen(navController: NavController, doctor: Doctor) {
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableStateOf(0) } // 0 = Información, 1 = Horarios

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFFF8F9FA))
    ) {
        // 🔹 Encabezado degradado
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
                    Image(
                        painter = rememberAsyncImagePainter(
                            if (doctor.imageUrl.isNotBlank()) doctor.imageUrl
                            else "https://cdn-icons-png.flaticon.com/512/2922/2922506.png"
                        ),
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
                                text = "${doctor.rating} (127)",
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

        // 🔹 Tabs: Información | Horarios
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

        // 🔹 Contenido dinámico según la pestaña seleccionada
        when (selectedTab) {
            0 -> {
                // 🟦 Información
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    InfoCard(
                        title = "Educación",
                        description = "Universidad Nacional Autónoma de México (UNAM)\nEspecialidad en ${doctor.specialty}"
                    )
                    InfoCard(
                        title = "Experiencia",
                        description = "+${doctor.experience} años de experiencia profesional\nCertificado por el Consejo Médico"
                    )
                    InfoCard(
                        title = "Idiomas",
                        description = "Español, Inglés"
                    )
                }
            }

            1 -> {
                // 🟩 Horarios
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    ScheduleCard("Lunes", "09:00 - 18:00")
                    ScheduleCard("Martes", "09:00 - 18:00")
                    ScheduleCard("Miércoles", "09:00 - 18:00")
                    ScheduleCard("Jueves", "09:00 - 18:00")
                    ScheduleCard("Viernes", "09:00 - 18:00")
                    ScheduleCard("Sábado", "09:00 - 14:00")
                    ScheduleCard("Domingo", "No atiende")
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
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
