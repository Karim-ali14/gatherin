package com.orwa.gatherin.model.other


import com.google.gson.annotations.SerializedName

data class UploadFileRes(
    @SerializedName("path")
    val path: String
)