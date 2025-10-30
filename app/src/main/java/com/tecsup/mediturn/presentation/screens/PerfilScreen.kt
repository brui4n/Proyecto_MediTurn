package com.tecsup.mediturn.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tecsup.mediturn.presentation.components.BottomBar
import com.tecsup.mediturn.navigation.Routes
import com.tecsup.mediturn.ui.theme.BluePrimary
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize
import androidx.compose.runtime.collectAsState
import com.tecsup.mediturn.data.remote.RetrofitInstance
import com.tecsup.mediturn.data.repository.PatientRepository
import com.tecsup.mediturn.data.session.SessionManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.Logout
import com.tecsup.mediturn.R

@Composable
fun PerfilScreen(navController: NavController) {
    var isEditing by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val session = remember { SessionManager(context) }
    val repo = remember { PatientRepository(RetrofitInstance.patientApi(context)) }
    val darkMode by session.darkModeEnabled.collectAsState(initial = false)

    // Datos reales del usuario
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    // Cambio de contraseña
    var showChangePassword by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var changing by remember { mutableStateOf(false) }
    var changeError by remember { mutableStateOf<String?>(null) }
    var changeDone by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        scope.launch {
            val user = session.userSession.firstOrNull() ?: return@launch
            try {
                val p = repo.getPatientById(user.id)
                nombre = p.name
                email = p.email
                telefono = p.phone
            } catch (_: Exception) {}
        }
    }

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController, currentRoute = Routes.Perfil.route)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // 🔹 Fondo degradado superior (más alto)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp) // 🔸 más grande que antes
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(BluePrimary, Color(0xFF4CAF50))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Mi perfil",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            // 🔹 Tarjeta principal (flotando encima del degradado)
            var mainCardHeightPx by remember { mutableStateOf(0) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = 130.dp) // 🔸 hace que la tarjeta suba sobre el degradado
                    .onGloballyPositioned { coords ->
                        mainCardHeightPx = coords.size.height
                    },
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Imagen circular
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter("https://cdn-icons-png.flaticon.com/512/2922/2922506.png"),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(90.dp)
                                .background(Color.Gray, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isEditing) {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre") }
                        )
                    } else {
                        Text(nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                    Text("Paciente activo", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isEditing) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Correo electrónico") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = telefono,
                            onValueChange = { telefono = it },
                            label = { Text("Teléfono") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        PerfilItem(icon = Icons.Default.Email, text = email)
                        PerfilItem(icon = Icons.Default.Call, text = telefono)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(onClick = { showChangePassword = !showChangePassword }) {
                        Icon(Icons.Default.Lock, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (showChangePassword) "Cerrar" else "Cambiar contraseña")
                    }

                    if (showChangePassword) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = currentPassword,
                            onValueChange = { currentPassword = it },
                            label = { Text("Contraseña actual") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("Nueva contraseña") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirmar nueva contraseña") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (changeError != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Error: $changeError", color = Color(0xFFB00020))
                        }
                        if (changeDone) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Contraseña actualizada", color = Color(0xFF2E7D32))
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (changing) return@Button
                                scope.launch {
                                    changeError = null
                                    changeDone = false
                                    if (newPassword != confirmPassword) {
                                        changeError = "Las contraseñas no coinciden"
                                        return@launch
                                    }
                                    changing = true
                                    val user = session.userSession.firstOrNull() ?: return@launch
                                    val ok = repo.changePassword(user.id, currentPassword, newPassword)
                                    changing = false
                                    changeDone = ok
                                    if (!ok) changeError = "No se pudo cambiar la contraseña"
                                    if (ok) { currentPassword = ""; newPassword = ""; confirmPassword = "" }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (changing) "Guardando..." else "Guardar nueva contraseña")
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (!isEditing) { isEditing = true } else {
                                // guardar cambios
                                scope.launch {
                                    val user = session.userSession.firstOrNull() ?: return@launch
                                    try {
                                        val updated = repo.updatePatient(user.id, nombre, email, telefono)
                                        session.updateBasicInfo(updated.name, updated.email)
                                        isEditing = false
                                    } catch (_: Exception) { /* puedes mostrar un snackbar */ }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (isEditing) "Guardar cambios" else "Editar perfil",
                            color = Color.White
                        )
                    }
                }
            }

            // Bloque externo: Modo oscuro (debajo del bloque de datos del paciente)
            val mainCardHeightDp = with(density) { mainCardHeightPx.toDp() }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = 130.dp + mainCardHeightDp + 16.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Modo oscuro", fontWeight = FontWeight.SemiBold)
                    Switch(
                        checked = darkMode,
                        onCheckedChange = { on -> scope.launch { session.setDarkMode(on) } }
                    )
                }
            }

            // Botón Cerrar sesión debajo del bloque de modo oscuro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = 130.dp + mainCardHeightDp + 16.dp + 88.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            session.clearSession()
                            navController.navigate(Routes.Login.route) {
                                popUpTo(Routes.Home.route) { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = Color(0xFFD32F2F)
                    )
                ) {
                    Icon(Icons.Filled.Logout, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar sesión")
                }
            }
        }
    }
}

@Composable
fun PerfilItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Icon(icon, contentDescription = null, tint = BluePrimary)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = MaterialTheme.colorScheme.onSurface)
    }
}
