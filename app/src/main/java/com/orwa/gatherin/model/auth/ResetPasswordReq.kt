package com.orwa.gatherin.model.auth


import com.google.gson.annotations.SerializedName

data class ResetPasswordReq(
    @SerializedName("email")
    val email: String,
    @SerializedName("newPassword")
    val newPassword: String,
    @SerializedName("passwordConfirmation")
    val confirm: String
)