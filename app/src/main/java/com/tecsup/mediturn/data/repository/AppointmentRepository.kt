package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.remote.AppointmentApi
import com.tecsup.mediturn.data.remote.dto.AppointmentCreateRequest

class AppointmentRepository(private val api: AppointmentApi) {

    suspend fun getAppointments(): List<Appointment> {
        return api.getAppointments()
    }

    suspend fun getAppointmentById(id: Int): Appointment {
        return api.getAppointment(id)
    }

    suspend fun createAppointment(request: AppointmentCreateRequest): Appointment {
        return api.createAppointment(request)
    }

    suspend fun getAppointmentsByPatient(patientId: Int, scope: String? = null): List<Appointment> {
        return api.getAppointmentsByPatient(patientId, scope)
    }

    suspend fun cancelAppointment(id: Int): Appointment = api.cancelAppointment(id)

    suspend fun rescheduleAppointment(id: Int, newSlotId: Int): Appointment =
        api.rescheduleAppointment(id, mapOf("slot" to newSlotId))
}