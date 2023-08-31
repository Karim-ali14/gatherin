package com.orwa.gatherin.model.group_results


import com.google.gson.annotations.SerializedName

data class FeatureX(
    @SerializedName("featureMark")
    val featureMark: String,
    @SerializedName("featureName")
    val featureName: String
)