package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.model.Slot
import com.tecsup.mediturn.data.repository.RetrofitInstance

class SlotRepository {

    suspend fun getSlots(): List<Slot> {
        return RetrofitInstance.api.getSlots()
    }

    suspend fun getSlotById(id: Int): Slot {
        return RetrofitInstance.api.getSlot(id)
    }
}