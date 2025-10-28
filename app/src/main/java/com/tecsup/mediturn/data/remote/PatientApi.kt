package com.tecsup.mediturn.data.remote

import com.google.gson.JsonObject
import com.tecsup.mediturn.data.model.Patient
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PatientApi {

    @GET("patients/")
    suspend fun getPatients(): List<Patient>

    @GET("patients/{id}/")
    suspend fun getPatient(@Path("id") id: Int): Patient

    @POST("login/")
    suspend fun loginPatient(
        @Body credentials: Map<String, String>
    ): Response<JsonObject>

    @POST("register/")
    suspend fun registerPatient(@Body data: Map<String, @JvmSuppressWildcards Any>): Response<Patient>

}