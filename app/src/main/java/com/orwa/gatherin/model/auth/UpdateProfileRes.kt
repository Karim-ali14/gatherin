package com.orwa.gatherin.model.auth


import com.google.gson.annotations.SerializedName

data class UpdateProfileRes(
    @SerializedName("email")
    val email: String,
    @SerializedName("firebaseToken")
    val firebaseToken: String?,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("picture")
    val picture: String?
)