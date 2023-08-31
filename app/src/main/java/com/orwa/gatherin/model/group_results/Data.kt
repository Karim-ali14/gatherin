package com.orwa.gatherin.model.group_results


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("feature_name")
    val featureName: String,
    @SerializedName("groups")
    val groups: List<GroupX>
)