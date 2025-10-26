package com.tecsup.mediturn.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecsup.mediturn.navigation.Routes
import com.tecsup.mediturn.presentation.components.BottomBar
import com.tecsup.mediturn.presentation.components.DoctorCard
import com.tecsup.mediturn.ui.theme.*
import com.tecsup.mediturn.viewmodel.HomeViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = viewModel()) {

    // Usamos el estado del ViewModel
    val searchQuery by viewModel.searchQuery
    val doctorsFiltered by viewModel.doctorsFiltered

    // Lista de especialidades
    val specialties = listOf(
        "Cardiología" to "❤️",
        "Neurología" to "🧠",
        "Oftalmología" to "👁️",
        "Traumatología" to "🦴",
        "Medicina General" to "🩺",
        "Pediatría" to "👶"
    )

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(bottom = 60.dp)
        ) {

            // Header con buscador
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        Brush.horizontalGradient(listOf(BluePrimary, GreenAccent)),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
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
                                "Bryan 👋",
                                color = WhiteText,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        IconButton(
                            onClick = { /* Notificaciones */ },
                            modifier = Modifier
                                .size(40.dp)
                                .background(WhiteTransparent, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = WhiteText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 🔍 Campo de búsqueda
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.onSearchQueryChanged(it) },
                            singleLine = true,
                            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "🔍 Buscar médicos o especialidades...",
                                        color = Color.Gray
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Si no hay búsqueda → mostramos especialidades
            if (searchQuery.isBlank()) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F4FD)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("📅 Próxima cita", color = BluePrimary, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Dra. María González", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("Cardiología", color = Color.Gray)
                        }

                        Button(
                            onClick = { /* Ver cita */ },
                            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                        ) {
                            Text("Ver")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Buscar por especialidad",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(specialties) { (name, icon) ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .height(90.dp)
                                .fillMaxWidth()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(icon, fontSize = 26.sp)
                                Text(name, color = Color.Black, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            } else {
                // Si hay búsqueda → mostramos doctores filtrados
                if (doctorsFiltered.isEmpty()) {
                    Text(
                        text = "No se encontraron resultados 😕",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(doctorsFiltered.size) { index ->
                            val doctor = doctorsFiltered[index]
                            DoctorCard(
                                doctor = doctor,
                                onClick = {
                                    // navController.navigate("${Routes.DoctorDetail.route}/${doctor.id}")
                                }
                            )
                        }
                    }
                }
            }
        }

        // BottomBar fija
        BottomBar(
            navController = navController,
            currentRoute = Routes.Home.route,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}