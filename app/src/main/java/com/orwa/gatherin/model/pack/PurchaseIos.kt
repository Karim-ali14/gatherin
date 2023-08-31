package com.orwa.gatherin.model.pack

import com.google.gson.annotations.SerializedName

data class PurchaseIos(
    @SerializedName("comment")
    val comment: String,
    @SerializedName("transactionReceipt")
    val transactionReceipt: String
)