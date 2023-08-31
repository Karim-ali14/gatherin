package com.orwa.gatherin.model.auth


import com.google.gson.annotations.SerializedName

data class VerifyCodeReq(
    @SerializedName("email")
    val email: String

)