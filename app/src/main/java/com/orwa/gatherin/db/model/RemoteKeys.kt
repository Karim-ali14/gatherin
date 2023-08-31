package com.orwa.gatherin.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeys(@PrimaryKey val notifyId: Int, val prevKey: Int?, val nextKey: Int?)
