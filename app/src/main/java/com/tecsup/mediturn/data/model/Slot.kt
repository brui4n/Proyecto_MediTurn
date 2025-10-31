package com.tecsup.mediturn.data.model

data class Slot(
    val id: Int,
    val doctor: Int,
    val date: String, // YYYY-MM-DD
    val time: String, // HH:MM
    val available: Boolean
)