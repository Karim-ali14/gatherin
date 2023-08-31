package com.orwa.gatherin.model.notify


import com.google.gson.annotations.SerializedName

data class SendNotifyReq(
    @SerializedName("body")
    val body: String,
    @SerializedName("groups")
    val groups: List<Int>
)