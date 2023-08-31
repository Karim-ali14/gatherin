package com.orwa.gatherin.model.fcm


import com.google.gson.annotations.SerializedName

data class FirebaseUserTokenReq(

    @SerializedName("token")
    val token: String?,
)