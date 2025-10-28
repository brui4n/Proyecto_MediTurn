package com.tecsup.mediturn.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.data.remote.RetrofitInstance
import com.tecsup.mediturn.data.repository.DoctorRepository
import com.tecsup.mediturn.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DoctorViewModel(application: Application) : AndroidViewModel(application) {

    private val doctorRepository = DoctorRepository(
        api = RetrofitInstance.doctorApi(application)
    )

    private val _doctors = MutableStateFlow<Resource<List<Doctor>>>(Resource.Loading())
    val doctors: StateFlow<Resource<List<Doctor>>> = _doctors

    fun loadDoctorsBySpecialty(specialty: String) {
        viewModelScope.launch {
            try {
                val response = doctorRepository.getDoctorsBySpecialty(specialty)
                _doctors.value = Resource.Success(response)
            } catch (e: Exception) {
                _doctors.value = Resource.Error(e.localizedMessage ?: "Error al obtener doctores")
            }
        }
    }

    fun searchDoctors(query: String) {
        viewModelScope.launch {
            try {
                val response = doctorRepository.searchDoctorsByName(query)
                _doctors.value = Resource.Success(response)
            } catch (e: Exception) {
                _doctors.value = Resource.Error(e.localizedMessage ?: "Error en la búsqueda")
            }
        }
    }
}