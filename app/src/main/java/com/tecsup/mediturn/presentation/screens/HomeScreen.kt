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
                                    .background(WhiteTransparent, CircleShape)
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
                                    text = { Text("Perfil") },
                                    onClick = { expanded = false /* Ir al perfil */ }
                                )
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
                            .background(Color.White),
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
                                tint = Color.Gray,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { viewModel.onSearchQueryChanged(it) },
                                singleLine = true,
                                textStyle = TextStyle(color = Color.Black, fontSize = 17.sp),
                                modifier = Modifier.weight(1f),
                                decorationBox = { innerTextField ->
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "Buscar médicos o especialidades...",
                                            color = Color.Gray.copy(alpha = 0.7f),
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
        containerColor = Color(0xFFF8F9FA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (searchQuery.isBlank()) {
                // 🗓️ Card próxima cita
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F4FD)),
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
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
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
                if (doctorsFiltered.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No se encontraron resultados 😕", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(doctorsFiltered.size) { index ->
                            val doctor = doctorsFiltered[index]
                            DoctorCard(
                                doctor = doctor,
                                onClick = { /* navController.navigate("${Routes.DoctorDetail.route}/${doctor.id}") */ }
                            )
                        }
                    }
                }

            }

        }
    }
}
