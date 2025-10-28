package com.tecsup.mediturn.data.model

data class LoginResponse(
    val patient: Patient,
    val access_token: String,
    val refresh_token: String
)