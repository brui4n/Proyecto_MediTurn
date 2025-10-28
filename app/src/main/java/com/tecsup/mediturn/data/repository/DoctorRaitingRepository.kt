package com.tecsup.mediturn.data.repository

import com.tecsup.mediturn.data.model.DoctorRating
import com.tecsup.mediturn.data.repository.RetrofitInstance

class DoctorRatingRepository {

    suspend fun getDoctorRatings(): List<DoctorRating> {
        return RetrofitInstance.api.getDoctorRatings()
    }

    suspend fun getRatingsByDoctor(doctorId: Int): List<DoctorRating> {
        return RetrofitInstance.api.getDoctorRatingsByDoctor(doctorId)
    }
}