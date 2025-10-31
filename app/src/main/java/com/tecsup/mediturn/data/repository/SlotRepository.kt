package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.model.Slot
import com.tecsup.mediturn.data.remote.SlotApi

class SlotRepository(private val api: SlotApi) {

    suspend fun getSlots(): List<Slot> {
        return api.getSlots()
    }

    suspend fun getSlotsByDoctor(doctorId: Int): List<Slot> {
        return api.getSlotsByDoctor(doctorId)
    }

    suspend fun getSlotsByDoctorAndDate(doctorId: Int, date: String, available: Boolean = true): List<Slot> {
        return api.getSlotsByDoctorAndDate(doctorId, date, available)
    }
}