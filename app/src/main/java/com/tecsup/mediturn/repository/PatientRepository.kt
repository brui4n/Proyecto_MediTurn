package com.tecsup.mediturn.repository

import com.tecsup.mediturn.data.model.Patient

/**
 * Archivo: PatientRepository.kt
 * Descripción: Repositorio local simulado para pacientes.
 *
 * Autor: Bryan Coronel
 * Proyecto: MediTurn
 * Fecha: Octubre 2025
 */
class PatientRepository {

    private val patients = mutableListOf(
        Patient(id = 1, name = "Bryan Coronel", age = 24, gender = "M", email = "bryan@example.com", phone = "987654321"),
        Patient(id = 2, name = "Milene Fuentes", age = 22, gender = "F", email = "milene@example.com", phone = "987654322"),
        Patient(id = 3, name = "Santiago Salas", age = 25, gender = "M", email = "santiago@example.com", phone = "987654323")
    )

    fun getAllPatients(): List<Patient> = patients.toList()

    fun getPatientById(id: Int): Patient? = patients.find { it.id == id }

    fun addPatient(patient: Patient) {
        patients.add(patient)
    }

    fun updatePatient(patient: Patient) {
        val idx = patients.indexOfFirst { it.id == patient.id }
        if (idx >= 0) patients[idx] = patient
    }

    // Buscar por nombre (simple)
    fun searchByName(query: String): List<Patient> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return getAllPatients()
        return patients.filter { it.name.lowercase().contains(q) }
    }
}