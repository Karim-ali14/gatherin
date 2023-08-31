package com.orwa.gatherin.model.chat


import com.google.gson.annotations.SerializedName

data class ChatItem(

    @SerializedName("id")
    val id: Int,
    @SerializedName("img")
    val img: String?,
    @SerializedName("msg")
    val msg: String?,
    @SerializedName("record")
    val record: String?,
    @SerializedName("room")
    val room: String,
    @SerializedName("senderId")
    val senderId: Int,
    @SerializedName("senderImage")
    val senderImage: String?,
    @SerializedName("senderName")
    val senderName: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("video")
    val video: String?,

    val question:Question?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
)