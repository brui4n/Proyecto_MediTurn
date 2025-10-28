package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.model.Patient
import com.tecsup.mediturn.data.repository.RetrofitInstance

class PatientRepository {

    suspend fun getPatients(): List<Patient> {
        return RetrofitInstance.api.getPatients()
    }

    suspend fun getPatientById(id: Int): Patient {
        return RetrofitInstance.api.getPatient(id)
    }
}