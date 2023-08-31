package com.orwa.gatherin.model.group


import com.google.gson.annotations.SerializedName

data class Member(
    @SerializedName("email")
    val email: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("id")
    val id: Int,

    val picture:String="",
    //Used to determine if the current group member is a leader
    var isLeader:Boolean=false
)