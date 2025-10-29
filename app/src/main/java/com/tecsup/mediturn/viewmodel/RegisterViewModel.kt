package com.tecsup.mediturn.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.mediturn.data.remote.RetrofitInstance
import com.tecsup.mediturn.data.repository.PatientRepository
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val patientRepo = PatientRepository(
        api = RetrofitInstance.patientApi(application)
    )

    var name = mutableStateOf("")
    var email = mutableStateOf("")
    var phone = mutableStateOf("")
    var gender = mutableStateOf("")
    var age = mutableStateOf("") // Edad como string para TextField
    var password = mutableStateOf("")
    var confirmPassword = mutableStateOf("")

    var errorMessage = mutableStateOf<String?>(null)
    var successMessage = mutableStateOf<String?>(null)
    var isRegistered = mutableStateOf(false)
    var isLoading = mutableStateOf(false)

    fun register() {
        val nameInput = name.value.trim()
        val emailInput = email.value.trim()
        val phoneInput = phone.value.trim()
        val genderInput = gender.value.trim()
        val ageInput = age.value.trim()
        val passwordInput = password.value.trim()
        val confirmInput = confirmPassword.value.trim()

        // Validación de campos vacíos
        if (nameInput.isEmpty() || emailInput.isEmpty() || phoneInput.isEmpty() ||
            genderInput.isEmpty() || ageInput.isEmpty() ||
            passwordInput.isEmpty() || confirmInput.isEmpty()
        ) {
            errorMessage.value = "Por favor completa todos los campos"
            return
        }

        // Validación de edad numérica
        val ageInt = ageInput.toIntOrNull()
        if (ageInt == null || ageInt <= 0) {
            errorMessage.value = "Ingresa una edad válida"
            return
        }

        // Validación de contraseñas
        if (passwordInput != confirmInput) {
            errorMessage.value = "Las contraseñas no coinciden"
            return
        }

        // Llamada al repositorio
        viewModelScope.launch {
            try {
                isLoading.value = true
                val success = patientRepo.register(
                    nameInput,
                    emailInput,
                    phoneInput,
                    genderInput,
                    passwordInput,
                    ageInt
                )

                if (success) {
                    isRegistered.value = true
                    successMessage.value = "Cuenta creada con éxito"
                    errorMessage.value = null
                } else {
                    errorMessage.value = "El correo ya está registrado"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = "Error al conectar con el servidor"
            } finally {
                isLoading.value = false
            }
        }
    }
}