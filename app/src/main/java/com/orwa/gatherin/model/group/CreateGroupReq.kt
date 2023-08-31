package com.orwa.gatherin.model.group


import com.google.gson.annotations.SerializedName

data class CreateGroupReq(
    @SerializedName("departmentId")
    val departmentId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("users")
    val users: List<Int>
)