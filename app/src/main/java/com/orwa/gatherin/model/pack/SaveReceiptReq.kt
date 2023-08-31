package com.orwa.gatherin.model.pack

import com.google.gson.annotations.SerializedName

data class SaveReceiptReq(
    @SerializedName("appType")
    val appType: String,
    @SerializedName("purchase")
    val purchase: Purchase
//    @SerializedName("purchase")
//    val purchase__ios: PurchaseIos?
)