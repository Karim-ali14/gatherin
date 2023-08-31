package com.orwa.gatherin.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class SectMemberModel(@PrimaryKey val id: Int, val deptId:Int, val name:String, val img:String, val isJoin:Boolean, var isRegistered:Boolean=false, val time:Long)