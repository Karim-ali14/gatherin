package com.orwa.gatherin.model.group_results


import com.google.gson.annotations.SerializedName

data class GroupX(
    @SerializedName("groupId")
    val groupId: Int,
    @SerializedName("value")
    val value: Int
)