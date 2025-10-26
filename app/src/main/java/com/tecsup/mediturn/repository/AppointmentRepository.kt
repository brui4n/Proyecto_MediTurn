package com.tecsup.mediturn.repository

import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.model.Doctor
import com.tecsup.mediturn.data.model.Patient
import com.tecsup.mediturn.data.model.Slot
import java.util.concurrent.atomic.AtomicInteger

/**
 * Archivo: AppointmentRepository.kt
 * Descripción: Repositorio local simulado para manejar citas (reservas).
 *              Este repo usa internamente Doctor/Patient/Slot como objetos completos
 *              (en un escenario real se linkearían por id o mediante una BD).
 *
 */
class AppointmentRepository(
    // Inyectar repositorios locales para poder resolver doctor/slot/patient
    private val doctorRepo: DoctorRepository,
    private val patientRepo: PatientRepository,
    private val slotRepo: SlotRepository
) {
    private val idGen = AtomicInteger(1)
    private val appointments = mutableListOf<Appointment>()

    fun getAllAppointments(): List<Appointment> = appointments.toList()

    fun getAppointmentsForPatient(patientId: Int): List<Appointment> =
        appointments.filter { it.patient.id == patientId }

    fun getAppointmentsForDoctor(doctorId: Int): List<Appointment> =
        appointments.filter { it.doctor.id == doctorId }

    /**
     * Crea una cita: marca el slot como no disponible y registra la cita.
     * Devuelve la Appointment creada o null si el slot no está disponible.
     */
    fun createAppointment(patientId: Int, doctorId: Int, slotId: Int): Appointment? {
        val patient: Patient = patientRepo.getPatientById(patientId) ?: return null
        val doctor: Doctor = doctorRepo.getDoctorById(doctorId) ?: return null
        val slot: Slot = slotRepo.findSlotById(slotId) ?: return null

        if (!slot.available) return null // slot ya reservado

        // marcar slot no disponible
        slotRepo.markSlotUnavailable(slotId)

        val appointment = Appointment(
            id = idGen.getAndIncrement(),
            doctor = doctor,
            patient = patient,
            slot = slot.copy(available = false),
            status = "Confirmada"
        )
        appointments.add(appointment)
        return appointment
    }

    fun cancelAppointment(appointmentId: Int): Boolean {
        val idx = appointments.indexOfFirst { it.id == appointmentId }
        if (idx < 0) return false
        val appt = appointments[idx]
        // liberar slot
        slotRepo.markSlotAvailable(appt.slot.id)
        // cambiar estado y eliminar o marcar cancelada
        appointments[idx] = appt.copy(status = "Cancelada")
        return true
    }

    /**
     * Reprogramar: intenta cambiar al nuevo slot (si está disponible).
     * Devuelve la appointment actualizada o null si fallo.
     */
    fun rescheduleAppointment(appointmentId: Int, newSlotId: Int): Appointment? {
        val idx = appointments.indexOfFirst { it.id == appointmentId }
        if (idx < 0) return null

        val appt = appointments[idx]
        val newSlot = slotRepo.findSlotById(newSlotId) ?: return null

        if (!newSlot.available) return null

        // liberar slot anterior
        slotRepo.markSlotAvailable(appt.slot.id)
        // marcar slot nuevo como no disponible
        slotRepo.markSlotUnavailable(newSlotId)

        val updated = appt.copy(slot = newSlot.copy(available = false), status = "Reprogramada")
        appointments[idx] = updated
        return updated
    }
}