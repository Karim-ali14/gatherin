package com.orwa.gatherin.utils

data class User internal constructor(
    val id: Int,
    val type:String?,
    var name: String?,
    val email: String?,
    var token: String?,
    var refreshToken:String?,
    var photoUrl: String?,
    val expiresAt: String?,
    val userName:String?,
    val password:String?,
    val gender:Int?,
    val birthday:String?,
    val address:String?,
    val phone:String?)
