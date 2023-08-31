package com.orwa.gatherin.model.teacher_home


import com.google.gson.annotations.SerializedName

data class UserInfoRes(
    @SerializedName("email")
    val email: String,
    @SerializedName("endDatePackage")
    val endDatePackage: String,
    @SerializedName("fullName")
    var fullName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("number_of_department")
    val numberOfDepartment: Int,
    @SerializedName("number_of_group")
    val numberOfGroup: Int,
    @SerializedName("pacakgeInfo")
    val pacakgeInfo: PacakgeInfo?,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("picture")
    var picture: String,
    @SerializedName("startDatePackage")
    val startDatePackage: String,
    @SerializedName("price")
    val price: Double,
)