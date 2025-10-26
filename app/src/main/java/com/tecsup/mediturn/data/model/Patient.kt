package com.tecsup.mediturn.data.model

data class Patient (
    val id: Int,
    val name: String,
    val age: Int,
    val gender: String,
    val email: String,
    val phone: String,
    val password: String
)