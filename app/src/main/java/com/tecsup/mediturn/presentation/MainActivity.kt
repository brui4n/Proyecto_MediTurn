package com.tecsup.mediturn.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tecsup.mediturn.navigation.NavGraph
import com.tecsup.mediturn.ui.theme.MediTurnTheme
import com.tecsup.mediturn.data.session.SessionManager
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val sessionManager = SessionManager(this)
            val darkMode by sessionManager.darkModeEnabled.collectAsState(initial = false)
            MediTurnTheme(darkTheme = darkMode) {
                NavGraph()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NavGraph()
}