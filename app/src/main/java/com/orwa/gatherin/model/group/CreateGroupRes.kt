package com.orwa.gatherin.model.group


import com.google.gson.annotations.SerializedName

data class CreateGroupRes(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("departmentId")
    val departmentId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)