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
import com.tecsup.mediturn.ui.theme.BluePrimary

data class Cita(
    val doctor: String,
    val especialidad: String,
    val fecha: String,
    val hora: String,
    val iniciales: String
)

@Composable
fun CitasScreen(navController: NavController) {

    var selectedTab by remember { mutableStateOf(0) } // 0 = próximas, 1 = historial

    val proximasCitas = listOf(
        Cita("Dra. María González", "Cardiología", "25/10/2025", "10:00 AM", "DMG"),
        Cita("Dr. Carlos Ramírez", "Neurología", "28/10/2025", "03:30 PM", "DCR")
    )

    val historialCitas = listOf(
        Cita("Dr. Juan Pérez", "Pediatría", "10/09/2025", "09:00 AM", "DJP"),
        Cita("Dra. Ana Torres", "Dermatología", "20/08/2025", "11:30 AM", "DAT")
    )

    val citas = if (selectedTab == 0) proximasCitas else historialCitas

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                currentRoute = Routes.Citas.route
            )
        },
        containerColor = Color(0xFFF8F9FA)
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
                containerColor = Color(0xFFEFF3F8),
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
                                color = if (selectedTab == index) Color.White else Color.DarkGray,
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
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(citas) { cita ->
                    CitaCard(cita)
                }
            }
        }
    }
}


@Composable
fun CitaCard(cita: Cita) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                    Text(cita.doctor, fontWeight = FontWeight.Bold)
                    Text(cita.especialidad, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = BluePrimary)
                Text(cita.fecha)
                Icon(Icons.Default.AccessTime, contentDescription = null, tint = BluePrimary)
                Text(cita.hora)
            }
        }
    }
}
