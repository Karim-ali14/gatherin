package com.orwa.gatherin.model.auth

import com.google.gson.annotations.SerializedName

data class VerifyCodeRes(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("code")
    val code: String
)