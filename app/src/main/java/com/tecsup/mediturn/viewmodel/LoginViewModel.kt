package com.tecsup.mediturn.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.mediturn.data.model.Patient
import com.tecsup.mediturn.data.remote.RetrofitInstance
import com.tecsup.mediturn.data.repository.PatientRepository
import com.tecsup.mediturn.data.session.SessionManager
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val repository = PatientRepository(
        api = RetrofitInstance.patientApi(application)
    )

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

                val response = repository.login(emailInput, passwordInput)

                if (response != null) {
                    loggedInUser.value = response.patient
                    errorMessage.value = null

                    sessionManager.saveUserSession(
                        response.patient.id,
                        response.patient.name,
                        response.patient.email,
                        response.access_token,
                        response.refresh_token
                    )
                } else {
                    errorMessage.value = "Credenciales incorrectas"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = "Error al conectar con el servidor"
            } finally {
                isLoading.value = false
            }
        }
    }

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

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            loggedInUser.value = null
        }
    }
}