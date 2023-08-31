package com.orwa.gatherin.rep

import com.orwa.gatherin.api.RetrofitService
import com.orwa.gatherin.model.auth.*
import com.skydoves.sandwich.ApiResponse
import okhttp3.ResponseBody
import javax.inject.Inject

class AuthRep @Inject constructor(
    private val service: RetrofitService
) {

    suspend fun signIn(signInReq:SignInReq):ApiResponse<SignInRes>{
        val res = service.signIn(signInReq)
        return res
    }

    suspend fun signUp(signUpReq: SignUpReq):ApiResponse<SignUpRes>{
        val res = service.signUp(signUpReq)
        return res
    }

    suspend fun verifyCode(verifyReq:VerifyCodeReq):ApiResponse<VerifyCodeRes>{
        val res = service.sendVerifyCode(verifyReq)
        return res
    }

    suspend fun verifyCodeForgot(verifyReq:VerifyCodeReq):ApiResponse<VerifyCodeRes>{
        val res = service.sendForgotPwdVerifyCode(verifyReq)
        return res
    }

    suspend fun resetPassword(req:ResetPasswordReq):ApiResponse<ResponseBody>{
        val res = service.resetPassword(req)
        return res
    }

}