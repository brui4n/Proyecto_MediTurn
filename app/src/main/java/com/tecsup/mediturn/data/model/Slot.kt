package com.tecsup.mediturn.data.model

data class Slot (
    val id: Int,
    val doctorId: Int,
    val date: String,
    val time: String,
    val available: Boolean
)