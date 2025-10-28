package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.data.remote.DoctorApi

class DoctorRepository(private val api: DoctorApi) {

    suspend fun getDoctors(): List<Doctor> {
        return api.getDoctors()
    }

    suspend fun getDoctorById(id: Int): Doctor {
        return api.getDoctor(id)
    }

    suspend fun getDoctorsBySpecialty(specialty: String): List<Doctor> {
        return api.getDoctorsBySpecialty(specialty)
    }

    suspend fun searchDoctorsByName(query: String): List<Doctor> {
        return api.searchDoctorsByName(query)
    }
}