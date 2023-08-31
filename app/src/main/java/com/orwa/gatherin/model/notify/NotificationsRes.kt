package com.orwa.gatherin.model.notify


import com.google.gson.annotations.SerializedName

data class NotificationsRes(
    @SerializedName("count_un_seen")
    val countUnSeen: Int,
    @SerializedName("notifications")
    val notifications: List<Notification>
)