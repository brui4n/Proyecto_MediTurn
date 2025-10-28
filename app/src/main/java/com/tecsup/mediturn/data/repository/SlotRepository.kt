package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.model.Slot
import com.tecsup.mediturn.data.remote.SlotApi

class SlotRepository(private val api: SlotApi) {

    suspend fun getSlots(): List<Slot> {
        return api.getSlots()
    }

    suspend fun getSlotById(id: Int): Slot {
        return api.getSlot(id)
    }
}