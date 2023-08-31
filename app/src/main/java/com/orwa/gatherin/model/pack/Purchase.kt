package com.orwa.gatherin.model.pack

import com.google.gson.annotations.SerializedName

data class Purchase(

    @SerializedName("productId")
    val productId: String,
    @SerializedName("purchaseToken")
    val purchaseToken: String
)