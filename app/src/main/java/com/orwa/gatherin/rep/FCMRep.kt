package com.orwa.gatherin.rep

import com.orwa.gatherin.api.RetrofitService
import com.orwa.gatherin.di.NetworkModule
import com.orwa.gatherin.model.fcm.FirebaseUserTokenReq

import com.skydoves.sandwich.ApiResponse
import okhttp3.ResponseBody
import javax.inject.Inject


class FCMRep @Inject constructor(
    @NetworkModule.Authenticated private val service: RetrofitService
) {

    suspend fun setFirebaseUserToken(req:FirebaseUserTokenReq):ApiResponse<ResponseBody>{
        val res = service.setFirebaseUserToken(req)
        return res
    }
}