package com.tecsup.mediturn.data.model

import com.google.gson.annotations.SerializedName

data class Appointment(
    val id: Int,
    val doctor: Doctor,
    val patient: Patient,
    val slot: Slot,
    val status: String,
    @SerializedName("consultation_type")
    val consultationType: String
)