package com.orwa.gatherin.model.group_results


import com.google.gson.annotations.SerializedName

data class DepartmentResultsResItem(
    @SerializedName("features")
    val features: List<FeatureX>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("totalMarks")
    val totalMarks: Int
)