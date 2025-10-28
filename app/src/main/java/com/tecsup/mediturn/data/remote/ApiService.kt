package com.tecsup.mediturn.data.remote

import com.tecsup.mediturn.data.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // DOCTORS
    @GET("doctors/")
    suspend fun getDoctors(): List<Doctor>

    @GET("doctors/{id}/")
    suspend fun getDoctor(@Path("id") id: Int): Doctor

    @GET("doctors/by_specialty/")
    suspend fun getDoctorsBySpecialty(@Query("specialty") specialty: String): List<Doctor>

    @GET("doctors/search/")
    suspend fun searchDoctorsByName(@Query("query") query: String): List<Doctor>

    // PATIENTS
    @GET("patients/")
    suspend fun getPatients(): List<Patient>

    @GET("patients/{id}/")
    suspend fun getPatient(@Path("id") id: Int): Patient

    // SLOTS
    @GET("slots/")
    suspend fun getSlots(): List<Slot>

    @GET("slots/{id}/")
    suspend fun getSlot(@Path("id") id: Int): Slot

    // APPOINTMENTS
    @GET("appointments/")
    suspend fun getAppointments(): List<Appointment>

    @GET("appointments/{id}/")
    suspend fun getAppointment(@Path("id") id: Int): Appointment

    // DOCTOR RATINGS
    @GET("doctor_ratings/")
    suspend fun getDoctorRatings(): List<DoctorRating>

    @GET("doctor_ratings/doctor/{doctorId}/")
    suspend fun getDoctorRatingsByDoctor(@Path("doctorId") doctorId: Int): List<DoctorRating>


}