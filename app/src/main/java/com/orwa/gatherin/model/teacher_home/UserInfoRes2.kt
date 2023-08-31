package com.orwa.gatherin.model.teacher_home


import com.google.gson.annotations.SerializedName

data class UserInfoRes2(
    @SerializedName("email")
    val email: String,
    @SerializedName("fullName")
    var fullName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("phone")
    var phone: String?,
    @SerializedName("picture")
    var picture: String,
    @SerializedName("number_of_department")
    var maxDepartments: Int?,
    @SerializedName("number_of_group")
    var maxGroups: Int?,
)