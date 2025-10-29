package com.tecsup.mediturn.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

    // Observar el estado desde el ViewModel
    val doctorState by viewModel.doctors.collectAsState()

    // Cargar doctores por especialidad al inicio
    LaunchedEffect(specialty) {
        viewModel.loadDoctorsBySpecialty(specialty)
    }

    // Buscar doctores al escribir
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            viewModel.searchDoctors(searchQuery)
        } else {
            viewModel.loadDoctorsBySpecialty(specialty)
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
                    placeholder = { Text("Buscar por nombre o especialidad...") },
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
                    val doctors = doctorState.data ?: emptyList()
                    Text(
                        text = "${doctors.size} médicos encontrados",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(doctors) { doctor ->
                            DoctorCard(
                                doctor = doctor,
                                onDetailClick = rememberUpdatedState {
                                    navController.navigate("${Routes.DoctorDetail.route}/${doctor.id}")
                                }.value
                            )
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