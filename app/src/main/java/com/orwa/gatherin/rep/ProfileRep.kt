package com.orwa.gatherin.rep

import com.orwa.gatherin.api.RetrofitService
import com.orwa.gatherin.di.NetworkModule
import com.orwa.gatherin.model.about.AboutAppResItem
import com.orwa.gatherin.model.auth.UpdateProfileRes
import com.orwa.gatherin.model.pack.PackageRes
import com.orwa.gatherin.model.pack.SaveReceiptReq
import com.orwa.gatherin.model.profile.PacksRes

import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

class ProfileRep @Inject constructor(
    @NetworkModule.Authenticated private val service: RetrofitService
) {

    suspend fun updateProfile(
        file: MultipartBody.Part,
        fullName: RequestBody,
        phone:RequestBody
    ): ApiResponse<UpdateProfileRes> {
        val res = service.updateProfile(file, fullName,phone)
        return res
    }

    suspend fun updateProfile(fullName: String, phone: String): ApiResponse<UpdateProfileRes> {
        val res = service.updateProfile(fullName, phone)
        return res
    }

    suspend fun getAboutApp(): ApiResponse<AboutAppResItem> {
        val res = service.getAboutApp()
        return res
    }

    suspend fun getAppPolicy(): ApiResponse<AboutAppResItem> {
        val res = service.getAppPolicy()
        return res
    }

    suspend fun getPackages(): ApiResponse<PacksRes> {
        val res = service.getPackages()
        return res
    }

    suspend fun updatePwd(oldPwd:String, newPwd:String, confirmPwd:String): ApiResponse<ResponseBody> {
        val res = service.updatePwd(oldPwd, newPwd, confirmPwd)
        return res
    }

    suspend fun buyPackage(packId:Int):ApiResponse<ResponseBody>{
        return service.buyPackage(packId)
    }

    suspend fun saveReceipt(req: SaveReceiptReq):ApiResponse<ResponseBody>{
        return service.saveReceipt(req)
    }


}