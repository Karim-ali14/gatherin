package com.orwa.gatherin.model.teacher_home


import com.google.gson.annotations.SerializedName

data class DepartmentsListResItem(
    @SerializedName("code")
    val code: String,
    @SerializedName("Groups")
    val groups: List<Group>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("User")
    val user: User,
    @SerializedName("url")
    val url: String,

    @SerializedName("user_counter")
    val membersCounter: Int,


    /**
     * used to know if the current department group results were updated from the teacher user role
     */

    var wasDone:Boolean = false
)