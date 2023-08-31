package com.orwa.gatherin.model.group


import com.google.gson.annotations.SerializedName

data class SendResponseToMasterReq(
    @SerializedName("groupId")
    val groupId: Int,
    @SerializedName("questionId")
    val questionId: Int,
    @SerializedName("body")
    val response: String
)