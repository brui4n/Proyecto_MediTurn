package com.tecsup.mediturn.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tecsup.mediturn.ui.theme.BluePrimary
import com.tecsup.mediturn.ui.theme.GreenAccent
import com.tecsup.mediturn.presentation.components.DoctorCard
import com.tecsup.mediturn.data.remote.RetrofitInstance
import com.tecsup.mediturn.data.repository.DoctorRepository

@Composable
fun CityFilterScreen(navController: NavController) {
    val repo = remember { DoctorRepository(RetrofitInstance.doctorApi(navController.context)) }
    var doctors by remember { mutableStateOf(listOf<com.tecsup.mediturn.data.model.Doctor>()) }
    var selectedCity by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(true) {
        try {
            doctors = repo.getDoctors()
        } catch (_: Exception) {}
    }

    val cities = remember(doctors) {
        doctors.mapNotNull { it.city?.takeIf { c -> c.isNotBlank() } }.distinct().sorted()
    }
    val visibleDoctors = remember(doctors, selectedCity) {
        if (selectedCity.isNullOrBlank()) doctors else doctors.filter { it.city.equals(selectedCity, true) }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(BluePrimary, GreenAccent)))
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .statusBarsPadding()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                    Text(text = "Filtrar por ciudad", color = Color.White, fontSize = 22.sp)
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                item {
                    OutlinedButton(
                        onClick = { selectedCity = null },
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selectedCity == null) BluePrimary else MaterialTheme.colorScheme.surface,
                            contentColor = if (selectedCity == null) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    ) { Text("Todas") }
                }
                items(cities) { city ->
                    OutlinedButton(
                        onClick = { selectedCity = city },
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selectedCity == city) BluePrimary else MaterialTheme.colorScheme.surface,
                            contentColor = if (selectedCity == city) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    ) { Text(city) }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxSize()) {
                items(visibleDoctors) { doctor ->
                    DoctorCard(doctor = doctor, onDetailClick = { navController.navigate("doctor/${doctor.id}") })
                }
            }
        }
    }
}


