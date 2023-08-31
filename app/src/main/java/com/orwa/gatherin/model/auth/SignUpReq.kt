package com.orwa.gatherin.model.auth


import com.google.gson.annotations.SerializedName

data class SignUpReq(
    @SerializedName("fullName")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("kind")
    val kind: String,

    @SerializedName("password")
    val password: String,
    @SerializedName("passwordConfirmation")
    val confirm: String,

)