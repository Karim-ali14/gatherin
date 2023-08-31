package com.orwa.gatherin.model.teacher_home


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email")
    val email: String,
    @SerializedName("fullName")
    val fullName: String
)