package com.tecsup.mediturn.data.remote

import com.google.gson.JsonObject
import com.tecsup.mediturn.data.model.Patient
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.PATCH

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

    @POST("change-password/")
    suspend fun changePassword(@Body data: Map<String, @JvmSuppressWildcards Any>): Response<JsonObject>

    @PATCH("patients/{id}/")
    suspend fun updatePatient(
        @Path("id") id: Int,
        @Body data: Map<String, @JvmSuppressWildcards Any>
    ): Patient

}