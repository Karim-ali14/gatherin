package com.orwa.gatherin.model.group_results


import com.google.gson.annotations.SerializedName

data class SendResultsReq(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("departmentId")
    val departmentId: Int
)