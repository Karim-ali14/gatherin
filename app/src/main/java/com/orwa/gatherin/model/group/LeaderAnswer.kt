package com.orwa.gatherin.model.group


import com.google.gson.annotations.SerializedName

data class LeaderAnswer(
    @SerializedName("answer")
    val answer: Answer,
    @SerializedName("leader")
    val leader: LeaderX
)