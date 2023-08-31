package com.orwa.gatherin.model.section


import com.orwa.gatherin.model.group.Master
import com.google.gson.annotations.SerializedName

data class GroupListResItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("members_count")
    val membersCount: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("join")
    val join: Boolean,
    @SerializedName("master")
    val master: Master?,

    )