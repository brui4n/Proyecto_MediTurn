package com.tecsup.mediturn.data.remote

import android.content.Context
import com.tecsup.mediturn.data.model.DoctorSchedule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.Properties
import com.tecsup.mediturn.data.session.SessionManager

object RetrofitInstance {

    private lateinit var retrofit: Retrofit

    fun getRetrofitInstance(context: Context): Retrofit {
        if (!::retrofit.isInitialized) {
            val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            val sessionManager = SessionManager(context)

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(AuthInterceptor(sessionManager)) // 🔹 interceptor de auth
                .build()

            val baseUrl: String = try {
                val props = Properties()
                val file = File("local.properties")
                if (file.exists()) {
                    props.load(file.inputStream())
                    props.getProperty("BASE_URL")?.takeIf { it.isNotBlank() } ?: "http://10.0.2.2:8000/api/"
                } else "http://10.0.2.2:8000/api/"
            } catch (e: Exception) {
                e.printStackTrace()
                "http://10.0.2.2:8000/api/"
            }

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }

    fun patientApi(context: Context): PatientApi =
        getRetrofitInstance(context).create(PatientApi::class.java)

    fun doctorApi(context: Context): DoctorApi =
        getRetrofitInstance(context).create(DoctorApi::class.java)

    fun slotApi(context: Context): SlotApi =
        getRetrofitInstance(context).create(SlotApi::class.java)

    fun appointmentApi(context: Context): AppointmentApi =
        getRetrofitInstance(context).create(AppointmentApi::class.java)

    fun doctorScheduleApi(context: Context): DoctorScheduleApi =
        getRetrofitInstance(context).create(DoctorScheduleApi::class.java)
}