package com.orwa.gatherin.model.pack


import com.google.gson.annotations.SerializedName
import com.orwa.gatherin.model.teacher_home.PacakgeInfo

data class PackageRes(

    @SerializedName("isValid")
    val isValid: Boolean,
    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("endDate")
    val endDate: String,

    @SerializedName("packageInfo")
    val packageInfo: PacakgeInfo
)