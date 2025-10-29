package com.tecsup.mediturn.data.model

data class DoctorSchedule(
    val id: Int,
    val doctor: Int,
    val weekday: Int,
    val weekday_name: String,
    val start_time: String,
    val end_time: String
)