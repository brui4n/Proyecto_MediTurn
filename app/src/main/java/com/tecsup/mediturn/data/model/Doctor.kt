package com.tecsup.mediturn.data.model

import com.google.gson.annotations.SerializedName

data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    @SerializedName("experience_desc")
    val experienceDesc: String?,
    val education: String?,
    val languages: String?,
    val rating: Float?,
    val image: String?,
    val city: String?
)