package com.orwa.gatherin.model.auth


import com.google.gson.annotations.SerializedName

data class SignInRes(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("token")
    val token: String,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("kind")
    val kind: String,
)