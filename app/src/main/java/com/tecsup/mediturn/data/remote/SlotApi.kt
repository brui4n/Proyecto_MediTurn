package com.tecsup.mediturn.data.remote

import com.tecsup.mediturn.data.model.Slot
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SlotApi {
    // SLOTS
    @GET("slots/")
    suspend fun getSlots(): List<Slot>

    // Trae los slots de un doctor específico
    @GET("slots/")
    suspend fun getSlotsByDoctor(
        @Query("doctor_id") doctorId: Int
    ): List<Slot>

    // Trae slots de un doctor en una fecha específica (opcionalmente solo disponibles)
    @GET("slots/")
    suspend fun getSlotsByDoctorAndDate(
        @Query("doctor_id") doctorId: Int,
        @Query("date") date: String,
        @Query("available") available: Boolean = true
    ): List<Slot>
}