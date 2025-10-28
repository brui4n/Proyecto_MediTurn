package com.tecsup.mediturn.data.remote

import com.tecsup.mediturn.data.model.Slot
import retrofit2.http.GET
import retrofit2.http.Path

interface SlotApi {
    // SLOTS
    @GET("slots/")
    suspend fun getSlots(): List<Slot>

    @GET("slots/{id}/")
    suspend fun getSlot(@Path("id") id: Int): Slot
}