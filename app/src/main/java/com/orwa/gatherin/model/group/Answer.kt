package com.orwa.gatherin.model.group


import com.google.gson.annotations.SerializedName

data class Answer(
    @SerializedName("body")
    val body: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("groupId")
    val groupId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("questionId")
    val questionId: Int,
    @SerializedName("updatedAt")
    val updatedAt: String
)