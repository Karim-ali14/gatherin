package com.orwa.gatherin.model.notify


import com.google.gson.annotations.SerializedName

data class NotificationsResPaginated(
    @SerializedName("count_un_seen")
    val countUnSeen: Int,
    @SerializedName("currentPage")
    val currentPage: Int,
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,
    @SerializedName("hasPreviousPage")
    val hasPreviousPage: Boolean,
    @SerializedName("lastPage")
    val lastPage: Int,
    @SerializedName("nextPage")
    val nextPage: Int?,
    @SerializedName("notifications")
    val notifications: List<Notification>,
    @SerializedName("previousPage")
    val previousPage: Int?
)