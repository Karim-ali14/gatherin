package com.orwa.gatherin.model.teacher_home


import com.google.gson.annotations.SerializedName

data class PacakgeInfo(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("expiry")
    val expiry: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("number_of_department")
    val numberOfDepartment: Int,
    @SerializedName("number_of_group")
    val numberOfGroup: Int,
    @SerializedName("number_of_students")
    val numberOfStudents: Int,
    @SerializedName("pack_android_ID")
    val packAndroidID: String?,
    @SerializedName("price")
    val price: Double,
    @SerializedName("private")
    val private: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)