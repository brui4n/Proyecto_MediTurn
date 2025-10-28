package com.tecsup.mediturn.data.repository

import android.content.Context
import com.tecsup.mediturn.data.model.DoctorSchedule
import com.tecsup.mediturn.data.remote.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DoctorScheduleRepository(private val context: Context) {

    private val api = RetrofitInstance.doctorScheduleApi(context)

    suspend fun getDoctorSchedulesByDoctor(doctorId: Int): List<DoctorSchedule> {
        return withContext(Dispatchers.IO) {
            try {
                api.getDoctorSchedulesByDoctor(doctorId)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList() // si falla la conexión, retornamos lista vacía
            }
        }
    }
}