package com.tecsup.mediturn.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.mediturn.data.model.Patient
import com.tecsup.mediturn.data.repository.PatientRepository
import com.tecsup.mediturn.data.session.SessionManager
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar el estado del Login y la sesión del usuario.
 *
 * Autor: Bryan Coronel
 * Proyecto: MediTurn
 * Fecha: Octubre 2025
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PatientRepository()
    private val sessionManager = SessionManager(application)

    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var loggedInUser = mutableStateOf<Patient?>(null)
    var errorMessage = mutableStateOf<String?>(null)
    var isLoading = mutableStateOf(false)

    fun login() {
        val emailInput = email.value.trim()
        val passwordInput = password.value.trim()

        if (emailInput.isEmpty() || passwordInput.isEmpty()) {
            errorMessage.value = "Por favor ingresa todos los campos"
            return
        }

        viewModelScope.launch {
            try {
                isLoading.value = true
                val patients = repository.getPatients()

                val user = patients.find {
                    it.email.equals(emailInput, ignoreCase = true) &&
                            it.password == passwordInput
                }

                if (user != null) {
                    loggedInUser.value = user
                    errorMessage.value = null

                    // Guardar sesión en DataStore
                    sessionManager.saveUserSession(user.id, user.name, user.email)
                } else {
                    errorMessage.value = "Correo o contraseña incorrectos"
                }

            } catch (e: Exception) {
                errorMessage.value = "Error al conectar con el servidor"
            } finally {
                isLoading.value = false
            }
        }
    }

    // 🔹 Cargar sesión almacenada (por ejemplo, en el SplashScreen)
    fun loadSession() {
        viewModelScope.launch {
            sessionManager.userSession.collect { session ->
                if (session != null) {
                    loggedInUser.value = Patient(
                        id = session.id,
                        name = session.name,
                        age = 0,
                        gender = "",
                        email = session.email,
                        phone = "",
                        password = ""
                    )
                }
            }
        }
    }

    // 🔹 Cerrar sesión
    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            loggedInUser.value = null
        }
    }
}