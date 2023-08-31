package com.orwa.gatherin.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members_remotes")
data class MembersRemoteKeys(@PrimaryKey val memberId: Int, val prevKey: Int?, val nextKey: Int?)
