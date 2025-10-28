package com.tecsup.mediturn.data.repository

import com.google.gson.Gson
import com.tecsup.mediturn.data.model.LoginResponse
import com.tecsup.mediturn.data.model.Patient
import com.tecsup.mediturn.data.remote.PatientApi
import retrofit2.Response

class PatientRepository(private val api: PatientApi) {

    private val gson = Gson()

    suspend fun register(
        name: String,
        email: String,
        phone: String,
        gender: String,
        password: String,
        ageInt: Int
    ): Boolean {
        return try {
            val data = mapOf<String, Any>(
                "name" to name,
                "email" to email,
                "phone" to phone,
                "gender" to gender,
                "password" to password,
                "age" to ageInt
            )
            val response: Response<Patient> = api.registerPatient(data)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun login(email: String, password: String): LoginResponse? {
        return try {
            val credentials = mapOf(
                "email" to email,
                "password" to password
            )
            val response = api.loginPatient(credentials)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!

                val patientJson = body.getAsJsonObject("patient")
                val patient = gson.fromJson(patientJson, Patient::class.java)

                val accessToken = body.get("access_token").asString
                val refreshToken = body.get("refresh_token").asString

                LoginResponse(patient, accessToken, refreshToken)
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}