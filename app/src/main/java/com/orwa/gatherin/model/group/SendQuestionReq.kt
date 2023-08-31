package com.orwa.gatherin.model.group


import com.google.gson.annotations.SerializedName

data class SendQuestionReq(
    @SerializedName("body")
    val body: String,
    @SerializedName("groups")
    val groups: List<Int>,
    @SerializedName("options")
    val options: List<String>?,
    @SerializedName("type")
    val type: String
)