package com.orwa.gatherin.model.profile


import com.google.gson.annotations.SerializedName

data class PacksResItem(
    @SerializedName("expiry")
    val expiry: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("number_of_department")
    val numberOfDepartment: Int,
    @SerializedName("number_of_group")
    val numberOfGroup: Int,
    @SerializedName("price")
    val price: Double,
    @SerializedName("image")
    val image: String,
    @SerializedName("pack_ios_ID")
    val iosPackID: String,
    @SerializedName("pack_android_ID")
    val packID: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("CanBuy")
    var canBuy: Boolean,

    @SerializedName("isSubscribed")
    var isSubscribed: Boolean=false,

)