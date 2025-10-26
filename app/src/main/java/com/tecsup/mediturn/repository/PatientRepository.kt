package com.tecsup.mediturn.repository

import com.tecsup.mediturn.data.model.Patient
import java.security.MessageDigest

/**
 * Archivo: PatientRepository.kt
 * Descripción: Repositorio local simulado para pacientes.
 *
 */
object PatientRepository {

    private val patients = mutableListOf(
        Patient(
            id = 1,
            name = "Bryan Coronel",
            age = 24,
            gender = "M",
            email = "bryan@example.com",
            phone = "987654321",
            password = "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4"
        ),
        Patient(
            id = 2,
            name = "Milene Fuentes",
            age = 22,
            gender = "F",
            email = "milene@example.com",
            phone = "987654322",
            password = "88d4266fd4e6338d13b845fcf289579d209c897823b9217da3e161936f031589"
        ),
        Patient(
            id = 3,
            name = "Santiago Salas",
            age = 25,
            gender = "M",
            email = "santiago@example.com",
            phone = "987654323",
            password = "d8578edf8458ce06fbc5bb76a58c5ca4a2d9b1fa"
        )
    )
    fun login(email: String, password: String): Patient? {
        val hashed = hashPassword(password)
        return patients.find { it.email == email && it.password == hashed }
    }

    fun register(name: String, email: String, phone: String, gender: String, password: String): Boolean {
        if (patients.any { it.email == email }) return false

        val newPatient = Patient(
            id = patients.size + 1,
            name = name,
            age = 0,
            gender = gender,
            email = email,
            phone = phone,
            password = hashPassword(password)
        )
        patients.add(newPatient)
        return true
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }


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