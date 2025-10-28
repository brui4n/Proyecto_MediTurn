package com.tecsup.mediturn.data.remote

import com.tecsup.mediturn.data.model.Doctor
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DoctorApi {

    @GET("doctors/")
    suspend fun getDoctors(): List<Doctor>

    @GET("doctors/{id}/")
    suspend fun getDoctor(@Path("id") id: Int): Doctor

    @GET("doctors/by_specialty/")
    suspend fun getDoctorsBySpecialty(@Query("specialty") specialty: String): List<Doctor>

    @GET("doctors/search/")
    suspend fun searchDoctorsByName(@Query("query") query: String): List<Doctor>
}