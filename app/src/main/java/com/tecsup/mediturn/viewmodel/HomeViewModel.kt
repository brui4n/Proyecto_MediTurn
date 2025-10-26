package com.tecsup.mediturn.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.repository.DoctorRepository

class HomeViewModel : ViewModel() {

    private val doctorRepo = DoctorRepository()

    // Estado de búsqueda
    var searchQuery = mutableStateOf("")

    // Lista de doctores filtrados
    var doctorsFiltered = mutableStateOf(doctorRepo.getAllDoctors())

    // Actualiza resultados cuando cambia el texto
    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
        doctorsFiltered.value =
            if (query.isBlank()) {
                doctorRepo.getAllDoctors()
            } else {
                val byName = doctorRepo.searchDoctorsByName(query)
                val bySpecialty = doctorRepo.findBySpecialty(query)
                (byName + bySpecialty).distinctBy { it.id }
            }
    }
}