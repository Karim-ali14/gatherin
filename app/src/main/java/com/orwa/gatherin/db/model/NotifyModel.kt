package com.orwa.gatherin.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotifyModel(@PrimaryKey val id: Int, val titleEn:String, val titleAr:String, val bodyEn:String, val bodyAr:String, val createdAt:String)