package com.tecsup.mediturn.data.model

import com.google.gson.annotations.SerializedName


data class DoctorRating(
    val id: Int,
    val doctor: Doctor,
    val patient: Patient,
    val score: Float,
    val comment: String?,
    @SerializedName("created_at")
    val createdAt: String
)