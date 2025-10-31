package com.tecsup.mediturn.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecsup.mediturn.navigation.Routes
import com.tecsup.mediturn.presentation.components.DoctorCard
import com.tecsup.mediturn.ui.theme.BluePrimary
import com.tecsup.mediturn.ui.theme.GreenAccent
import com.tecsup.mediturn.util.Resource
import com.tecsup.mediturn.viewmodel.DoctorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorListScreen(
    navController: NavController,
    specialty: String,
    viewModel: DoctorViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf<String?>(null) }

    // Observar el estado desde el ViewModel
    val doctorState by viewModel.doctors.collectAsState()

    // Cargar doctores por especialidad al inicio
    LaunchedEffect(specialty) {
        viewModel.loadDoctorsBySpecialty(specialty)
    }

    // Lista de doctores de la especialidad (sin filtros)
    val allSpecialtyDoctors = remember(doctorState) {
        if (doctorState is Resource.Success) {
            doctorState.data ?: emptyList()
        } else {
            emptyList()
        }
    }

    // Filtrar localmente por búsqueda y ciudad
    val filteredDoctors = remember(allSpecialtyDoctors, searchQuery, selectedCity) {
        allSpecialtyDoctors.filter { doctor ->
            // Filtro por búsqueda (solo nombre)
            val matchesSearch = if (searchQuery.isBlank()) {
                true
            } else {
                val query = searchQuery.lowercase()
                doctor.name.lowercase().contains(query)
            }
            
            // Filtro por ciudad
            val matchesCity = if (selectedCity.isNullOrBlank()) {
                true
            } else {
                doctor.city?.equals(selectedCity, ignoreCase = true) == true
            }
            
            matchesSearch && matchesCity
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(BluePrimary, GreenAccent)
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .statusBarsPadding()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Buscar médicos",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = Color.Gray
                        )
                    },
                    placeholder = { Text("Buscar por nombre...") },
                    shape = MaterialTheme.shapes.large,
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            when (doctorState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = BluePrimary)
                    }
                }

                is Resource.Success -> {
                    // Ciudades disponibles basadas en todos los doctores de la especialidad
                    val cities = remember(allSpecialtyDoctors) {
                        allSpecialtyDoctors.mapNotNull { it.city?.takeIf { c -> c.isNotBlank() } }.distinct().sorted()
                    }
                    
                    Text(
                        text = "${filteredDoctors.size} médicos encontrados",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (cities.isNotEmpty()) {
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
                    }

                    if (filteredDoctors.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (searchQuery.isNotBlank() || !selectedCity.isNullOrBlank()) 
                                    "No se encontraron resultados" 
                                else 
                                    "No hay médicos disponibles",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(filteredDoctors) { doctor ->
                                DoctorCard(
                                    doctor = doctor,
                                    onDetailClick = {
                                        navController.navigate("${Routes.DoctorDetail.route}/${doctor.id}")
                                    }
                                )
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = doctorState.message ?: "Error al cargar médicos",
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}