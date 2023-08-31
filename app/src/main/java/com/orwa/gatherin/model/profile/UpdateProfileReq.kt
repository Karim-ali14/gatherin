package com.orwa.gatherin.model.profile

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class UpdateProfileReq (

    @SerializedName("avatar")
    val avatar: MultipartBody.Part,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("phone")
    val phone: String
        )