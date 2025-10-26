package com.tecsup.mediturn.repository

import com.tecsup.mediturn.data.model.Slot
import java.util.concurrent.atomic.AtomicInteger

/**
 * Archivo: SlotRepository.kt
 * Descripción: Repositorio local simulado que maneja los slots (horarios) de los doctores.
 */
class SlotRepository {

    // Generador simple de IDs
    private val idGen = AtomicInteger(1)

    // Lista mutable de slots (simulados)
    private val slots = mutableListOf<Slot>().apply {
        // Ejemplos de slots (doctorId, date, time)
        add(Slot(id = idGen.getAndIncrement(), doctorId = 1, date = "2025-11-01", time = "09:00", available = true))
        add(Slot(id = idGen.getAndIncrement(), doctorId = 1, date = "2025-11-01", time = "09:30", available = true))
        add(Slot(id = idGen.getAndIncrement(), doctorId = 1, date = "2025-11-01", time = "10:00", available = true))

        add(Slot(id = idGen.getAndIncrement(), doctorId = 2, date = "2025-11-01", time = "11:00", available = true))
        add(Slot(id = idGen.getAndIncrement(), doctorId = 2, date = "2025-11-02", time = "08:30", available = true))

        add(Slot(id = idGen.getAndIncrement(), doctorId = 3, date = "2025-11-03", time = "14:00", available = true))
    }

    fun getAllSlots(): List<Slot> = slots.toList()

    fun getSlotsForDoctor(doctorId: Int): List<Slot> =
        slots.filter { it.doctorId == doctorId }

    fun getAvailableSlotsForDoctorOnDate(doctorId: Int, date: String): List<Slot> =
        slots.filter { it.doctorId == doctorId && it.date == date && it.available }

    fun findSlotById(slotId: Int): Slot? = slots.find { it.id == slotId }

    fun markSlotUnavailable(slotId: Int) {
        val idx = slots.indexOfFirst { it.id == slotId }
        if (idx >= 0) slots[idx] = slots[idx].copy(available = false)
    }

    fun markSlotAvailable(slotId: Int) {
        val idx = slots.indexOfFirst { it.id == slotId }
        if (idx >= 0) slots[idx] = slots[idx].copy(available = true)
    }

    // Agregar un nuevo slot (útil para tests)
    fun addSlot(doctorId: Int, date: String, time: String): Slot {
        val new = Slot(id = idGen.getAndIncrement(), doctorId = doctorId, date = date, time = time, available = true)
        slots.add(new)
        return new
    }
}