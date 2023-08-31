package com.orwa.gatherin.model.about


import com.google.gson.annotations.SerializedName

data class AboutAppResItem(
    @SerializedName("body_ar")
    val bodyAr: String,
    @SerializedName("body_en")
    val bodyEn: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("updatedAt")
    val updatedAt: String
)