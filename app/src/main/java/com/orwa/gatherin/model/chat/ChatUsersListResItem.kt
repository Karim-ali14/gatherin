package com.orwa.gatherin.model.chat


import com.google.gson.annotations.SerializedName

data class ChatUsersListResItem(
    @SerializedName("email")
    val email: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("picture")
    val picture: String,

    @SerializedName("countOfUnReadChat")
    val countOfUnReadChat: Int

)