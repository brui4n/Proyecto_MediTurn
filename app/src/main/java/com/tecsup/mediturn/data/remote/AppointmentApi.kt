package com.tecsup.mediturn.data.remote

import com.tecsup.mediturn.data.model.Appointment
import com.tecsup.mediturn.data.remote.dto.AppointmentCreateRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AppointmentApi {

    @GET("appointments/")
    suspend fun getAppointments(): List<Appointment>

    @GET("appointments/{id}/")
    suspend fun getAppointment(@Path("id") id: Int): Appointment

    @GET("appointments/")
    suspend fun getAppointmentsByPatient(
        @Query("patient") patientId: Int,
        @Query("scope") scope: String? = null // 'upcoming' | 'past'
    ): List<Appointment>

    // Usa la acción personalizada del backend para crear citas
    @POST("appointments/create-appointment/")
    suspend fun createAppointment(@Body request: AppointmentCreateRequest): Appointment

    @POST("appointments/{id}/cancel/")
    suspend fun cancelAppointment(@Path("id") id: Int): Appointment

    @POST("appointments/{id}/reschedule/")
    suspend fun rescheduleAppointment(
        @Path("id") id: Int,
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): Appointment
}