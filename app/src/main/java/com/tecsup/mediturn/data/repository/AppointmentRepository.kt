package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.remote.AppointmentApi

class AppointmentRepository(private val api: AppointmentApi) {

    suspend fun getAppointments(): List<Appointment> {
        return api.getAppointments()
    }

    suspend fun getAppointmentById(id: Int): Appointment {
        return api.getAppointment(id)
    }
}