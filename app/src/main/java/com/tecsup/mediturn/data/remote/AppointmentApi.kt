package com.tecsup.mediturn.data.remote

import com.tecsup.mediturn.data.model.Appointment
import retrofit2.http.GET
import retrofit2.http.Path

interface AppointmentApi {

    @GET("appointments/")
    suspend fun getAppointments(): List<Appointment>

    @GET("appointments/{id}/")
    suspend fun getAppointment(@Path("id") id: Int): Appointment
}