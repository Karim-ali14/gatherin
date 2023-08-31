package com.orwa.gatherin.model.section


import com.google.gson.annotations.SerializedName

data class DeleteSectionMemberReq(
    @SerializedName("users")
    val users: List<Int>,
    @SerializedName("departmentId")
    val departmentId: Int)