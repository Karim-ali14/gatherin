package com.orwa.gatherin.model.auth


import com.google.gson.annotations.SerializedName

data class SignUpRes(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("token")
    val token: String,
    @SerializedName("userId")
    val userId: Int
)