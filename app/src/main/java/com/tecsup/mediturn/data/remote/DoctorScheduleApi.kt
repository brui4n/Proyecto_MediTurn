package com.tecsup.mediturn.data.remote

import com.tecsup.mediturn.data.model.DoctorSchedule
import retrofit2.http.GET
import retrofit2.http.Query

interface DoctorScheduleApi {

    // Ejemplo: /api/doctor-schedules/?doctor=3
    @GET("doctor-schedules/")
    suspend fun getDoctorSchedulesByDoctor(
        @Query("doctor") doctorId: Int
    ): List<DoctorSchedule>
}