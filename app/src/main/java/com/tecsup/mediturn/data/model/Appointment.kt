package com.tecsup.mediturn.data.model

data class Appointment (
    val id: Int,
    val doctor: Doctor,
    val patient: Patient,
    val slot: Slot,
    val status: String
)