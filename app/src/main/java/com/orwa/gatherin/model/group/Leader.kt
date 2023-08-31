package com.orwa.gatherin.model.group


import com.google.gson.annotations.SerializedName

data class Leader(
    @SerializedName("email")
    val email: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("id")
    val id: Int
)