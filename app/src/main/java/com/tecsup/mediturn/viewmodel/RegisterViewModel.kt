package com.tecsup.mediturn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.mediturn.data.model.Patient
import com.tecsup.mediturn.repository.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: PatientRepository = PatientRepository
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    private val _isRegistered = MutableStateFlow(false)
    val isRegistered: StateFlow<Boolean> = _isRegistered

    fun registerPatient(
        fullName: String,
        email: String,
        phone: String,
        gender: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            if (fullName.isBlank() || email.isBlank() || phone.isBlank() ||
                gender.isBlank() || password.isBlank() || confirmPassword.isBlank()
            ) {
                _errorMessage.value = "Por favor completa todos los campos"
                _successMessage.value = null
                return@launch
            }

            if (password != confirmPassword) {
                _errorMessage.value = "Las contraseñas no coinciden"
                _successMessage.value = null
                return@launch
            }

            val success = repository.register(fullName, email, phone, gender, password)

            if (success) {
                _isRegistered.value = true
                _successMessage.value = "Cuenta creada con éxito"
                _errorMessage.value = null
            } else {
                _errorMessage.value = "El correo ya está registrado"
                _successMessage.value = null
            }
        }
    }
}