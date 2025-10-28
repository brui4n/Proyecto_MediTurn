package com.tecsup.mediturn.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.data.repository.DoctorRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val doctorRepo = DoctorRepository()

    // Estado de búsqueda
    var searchQuery = mutableStateOf("")

    // Lista de doctores filtrados (inicialmente vacía hasta cargar desde API)
    var doctorsFiltered = mutableStateOf<List<Doctor>>(emptyList())

    init {
        // Cargar doctores al iniciar el ViewModel
        loadAllDoctors()
    }

    private fun loadAllDoctors() {
        viewModelScope.launch {
            try {
                val list = doctorRepo.getDoctors()
                doctorsFiltered.value = list
            } catch (e: Exception) {
                // En caso de error, dejamos la lista vacía (puedes manejar un estado de error si lo deseas)
                doctorsFiltered.value = emptyList()
            }
        }
    }

    // Actualiza resultados cuando cambia el texto de búsqueda
    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query

        viewModelScope.launch {
            try {
                if (query.isBlank()) {
                    // Si no hay query, mostramos todos
                    doctorsFiltered.value = doctorRepo.getDoctors()
                } else {
                    // Buscamos por nombre y por especialidad y combinamos resultados sin duplicados
                    val byName = try { doctorRepo.searchDoctorsByName(query) } catch (e: Exception) { emptyList() }
                    val bySpecialty = try { doctorRepo.getDoctorsBySpecialty(query) } catch (e: Exception) { emptyList() }

                    val combined = (byName + bySpecialty).distinctBy { it.id }
                    doctorsFiltered.value = combined
                }
            } catch (e: Exception) {
                doctorsFiltered.value = emptyList()
            }
        }
    }
}