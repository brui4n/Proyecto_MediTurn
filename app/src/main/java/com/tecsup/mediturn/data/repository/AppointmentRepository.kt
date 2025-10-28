package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.repository.RetrofitInstance

class AppointmentRepository {

    suspend fun getAppointments(): List<Appointment> {
        return RetrofitInstance.api.getAppointments()
    }

    suspend fun getAppointmentById(id: Int): Appointment {
        return RetrofitInstance.api.getAppointment(id)
    }
}