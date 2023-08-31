package com.orwa.gatherin.model.group


import com.google.gson.annotations.SerializedName

data class GroupDetailsRes(
    @SerializedName("departmentId")
    val departmentId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("leader")
    val leader:Leader?,
    @SerializedName("join")
    val join: Boolean,
    @SerializedName("master")
    val master: Master?,
    @SerializedName("members")
    val members: List<Member>,
    @SerializedName("members_count")
    val membersCount: Int,
    @SerializedName("name")
    val name: String
)