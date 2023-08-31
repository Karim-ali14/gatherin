package com.orwa.gatherin.model.group


import com.google.gson.annotations.SerializedName

data class UpdateGroupReq(
    @SerializedName("groupId")
    val groupId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("users")
    val users: List<Int>
)