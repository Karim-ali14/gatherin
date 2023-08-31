package com.orwa.gatherin.model.notify


import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("body_ar")
    val bodyAr: String,
    @SerializedName("body_en")
    val bodyEn: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("title_ar")
    val titleAr: String,
    @SerializedName("title_en")
    val titleEn: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("userId")
    val userId: Int
)