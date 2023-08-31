package com.orwa.gatherin.model.teacher_home


import com.google.gson.annotations.SerializedName

data class Group(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,

    @SerializedName("members_count")
    val membersCount: Int=0,
)