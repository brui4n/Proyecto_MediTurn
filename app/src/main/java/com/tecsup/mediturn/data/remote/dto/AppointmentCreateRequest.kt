package com.tecsup.mediturn.data.remote.dto

data class AppointmentCreateRequest(
    val doctor: Int,
    val patient: Int,
    val slot: Int,
    val consultation_type: String,
    val status: String = "Pendiente"
)


