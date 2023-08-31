package com.orwa.gatherin.model.chat


data class ReportReq(
    val message: String,
    val userId: String,
    var groupId: String
)