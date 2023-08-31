package com.orwa.gatherin.model.section


import com.google.gson.annotations.SerializedName
import com.orwa.gatherin.model.group.GroupMembersResItem

data class SectMembersPaginatedRes(
    @SerializedName("currentPage")
    val currentPage: Int,
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,
    @SerializedName("hasPreviousPage")
    val hasPreviousPage: Boolean,
    @SerializedName("lastPage")
    val lastPage: Int,
    @SerializedName("member")
    val member: List<Member>,
    @SerializedName("members_count")
    val membersCount: Int,
    @SerializedName("nextPage")
    val nextPage: Int?,
    @SerializedName("previousPage")
    val previousPage: Any
)