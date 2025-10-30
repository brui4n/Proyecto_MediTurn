package com.tecsup.mediturn.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.ui.theme.BluePrimary
import com.tecsup.mediturn.ui.theme.GreenAccent
import com.tecsup.mediturn.data.remote.RetrofitInstance
import com.tecsup.mediturn.data.repository.SlotRepository
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color

@Composable
fun DoctorCard(
    doctor: Doctor,
    onDetailClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var availableToday by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(doctor.id) {
        scope.launch {
            try {
                val repo = SlotRepository(RetrofitInstance.slotApi(context))
                val today = try {
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                } catch (e: Exception) { null }
                availableToday = if (today == null) null else repo.getSlotsByDoctorAndDate(doctor.id, today, true).isNotEmpty()
            } catch (_: Exception) {
                availableToday = null
            }
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Imagen del doctor
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

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = doctor.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = doctor.specialty,
                        color = MaterialTheme.colorScheme.primary
                    )


                    Spacer(modifier = Modifier.height(4.dp))

                    // Ciudad (opcional)
                    doctor.city?.let {
                        Text(
                            text = it,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Disponibilidad basada en slots de hoy
                    when (availableToday) {
                        true -> Text(
                            text = "Disponible hoy",
                            color = GreenAccent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        false -> Text(
                            text = "No disponible hoy",
                            color = Color(0xFFB00020),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        null -> { /* sin texto mientras carga o en error */ }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón de detalle
            Button(
                onClick = onDetailClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BluePrimary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Ver detalle")
            }
        }
    }
}