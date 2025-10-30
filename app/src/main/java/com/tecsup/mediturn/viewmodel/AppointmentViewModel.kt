package com.tecsup.mediturn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.repository.AppointmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppointmentViewModel(
    private val repository: AppointmentRepository
) : ViewModel() {

    // Estados observables
    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments

    private val _selectedAppointment = MutableStateFlow<Appointment?>(null)
    val selectedAppointment: StateFlow<Appointment?> = _selectedAppointment

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // 🔹 Obtener todas las citas
    fun loadAppointments() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _appointments.value = repository.getAppointments()
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar citas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 🔹 Obtener una cita específica
    fun loadAppointmentById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _selectedAppointment.value = repository.getAppointmentById(id)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar cita: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}