package com.orwa.gatherin.model.group_results


import com.orwa.gatherin.present.teacher.home.group_results.TeacherGroupsResultsRecyclerViewAdapter

data class Feature(
//    @SerializedName("id")
//    val id: Int,
//    @SerializedName("title")
    var title: String,
//    @SerializedName("Values")
    var values: List<Value>,

    var type: TeacherGroupsResultsRecyclerViewAdapter.ItemType,

    var isFixed:Boolean
)