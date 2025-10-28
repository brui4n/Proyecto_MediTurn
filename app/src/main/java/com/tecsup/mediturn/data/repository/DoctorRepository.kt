package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.data.repository.RetrofitInstance

class DoctorRepository {

    suspend fun getDoctors(): List<Doctor> {
        return RetrofitInstance.api.getDoctors()
    }

    suspend fun getDoctorById(id: Int): Doctor {
        return RetrofitInstance.api.getDoctor(id)
    }

    suspend fun getDoctorsBySpecialty(specialty: String): List<Doctor> {
        return emptyList()
    }

    suspend fun searchDoctorsByName(query: String): List<Doctor> {
        return emptyList()
    }
}