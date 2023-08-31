package com.orwa.gatherin.model.section

import com.google.gson.annotations.SerializedName

data class CreateSectionReq(
    @SerializedName("name")
    val name: String,
    @SerializedName("code")
    val code: String
)
