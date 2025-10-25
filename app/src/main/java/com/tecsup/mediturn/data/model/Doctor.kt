package com.tecsup.mediturn.data.model

data class Doctor (
    val id: Int,
    val name: String,
    val specialty: String,
    val experience: Int,
    val rating: Double,
    val imageUrl: String
)