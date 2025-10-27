package com.tecsup.mediturn.presentation.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tecsup.mediturn.navigation.Routes
import com.tecsup.mediturn.ui.theme.*
import com.tecsup.mediturn.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun MediTurnLogo() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .width(160.dp)
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🩺",
                fontSize = 45.sp,
                color = BluePrimary
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "💚",
                fontSize = 40.sp,
                color = GreenAccent
            )
        }
    }
}

@Composable
fun SplashScreen(navController: NavController, viewModel: SplashViewModel) {
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()

    val gradientBrush = Brush.verticalGradient(colors = BackgroundGradient)

    // Efecto para navegar cuando tengamos el estado listo
    LaunchedEffect(isUserLoggedIn) {
        if (isUserLoggedIn != null) {
            delay(2000L)
            navController.popBackStack()
            if (isUserLoggedIn == true) {
                navController.navigate(Routes.Home.route)
            } else {
                navController.navigate(Routes.Login.route)
            }
        }
    }

    // Pantalla visual (tu mismo diseño)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 64.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                MediTurnLogo()
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "MediTurn",
                    color = WhiteText,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tu salud, nuestra prioridad",
                    color = WhiteTransparent,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    DotIndicator(isActive = index == 1)
                    if (index < 2) Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
    }
}

@Composable
fun DotIndicator(isActive: Boolean) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isActive) 1f else 0.3f,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier
            .size(10.dp)
            .background(
                color = WhiteText.copy(alpha = animatedAlpha),
                shape = RoundedCornerShape(50)
            )
    )
}