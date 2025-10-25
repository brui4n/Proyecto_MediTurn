package com.tecsup.mediturn.repository

import com.tecsup.mediturn.data.model.Doctor

/**
 * Archivo: DoctorRepository.kt
 * Descripción: Repositorio local simulado para doctores.
 *
 * Autor: Bryan Coronel
 * Proyecto: MediTurn
 * Fecha: Octubre 2025
 */
class DoctorRepository {

    // Datos simulados iniciales
    private val doctors = mutableListOf(
        Doctor(
            id = 1,
            name = "Dr. Juan Pérez",
            specialty = "Cardiología",
            experience = 10,
            rating = 4.7,
            imageUrl = ""
        ),
        Doctor(
            id = 2,
            name = "Dra. Ana Torres",
            specialty = "Pediatría",
            experience = 6,
            rating = 4.5,
            imageUrl = ""
        ),
        Doctor(
            id = 3,
            name = "Dr. Luis García",
            specialty = "Dermatología",
            experience = 8,
            rating = 4.2,
            imageUrl = ""
        ),
        Doctor(
            id = 4,
            name = "Dra. Carla Medina",
            specialty = "Ginecología",
            experience = 12,
            rating = 4.8,
            imageUrl = ""
        ),
        Doctor(
            id = 5,
            name = "Dr. Marco Rojas",
            specialty = "Medicina General",
            experience = 4,
            rating = 4.0,
            imageUrl = ""
        )
    )

    fun getAllDoctors(): List<Doctor> = doctors.toList()

    fun getDoctorById(id: Int): Doctor? = doctors.find { it.id == id }

    fun searchDoctorsByName(query: String): List<Doctor> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return getAllDoctors()
        return doctors.filter { it.name.lowercase().contains(q) }
    }

    fun findBySpecialty(specialty: String): List<Doctor> {
        val s = specialty.trim()
        if (s.isEmpty()) return getAllDoctors()
        return doctors.filter { it.specialty.contains(s, ignoreCase = true) }
    }

    // Para futuras operaciones (agregar, editar)
    fun addDoctor(doctor: Doctor) {
        doctors.add(doctor)
    }

    fun updateDoctor(updated: Doctor) {
        val idx = doctors.indexOfFirst { it.id == updated.id }
        if (idx >= 0) doctors[idx] = updated
    }
}