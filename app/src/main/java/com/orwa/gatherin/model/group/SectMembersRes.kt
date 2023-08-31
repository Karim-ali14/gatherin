package com.orwa.gatherin.model.group

import com.google.gson.annotations.SerializedName


class SectMembersRes(
    @SerializedName("member")
    val members: List<GroupMembersResItem>,
    @SerializedName("members_count")
    val membersCount: Int)

