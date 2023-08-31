package com.orwa.gatherin.model.section


import com.google.gson.annotations.SerializedName


data class Member(
    @SerializedName("id")
    val id: Int,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("isJoin")
    val isJoin: Boolean
)