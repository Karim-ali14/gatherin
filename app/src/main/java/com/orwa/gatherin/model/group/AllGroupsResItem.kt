package com.orwa.gatherin.model.group


import com.google.gson.annotations.SerializedName

data class AllGroupsResItem(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("departmentId")
    val departmentId: Int,

    @SerializedName("Department.name")
    val departmentName: String,

    @SerializedName("id")
    val id: Int,
    @SerializedName("leader")
    val leader: LeaderX,
    @SerializedName("master")
    val master: Any,
    @SerializedName("name")
    val name: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("isAnswer")
    val isAnswer: Boolean,

    var isSelected:Boolean=false
)