/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.orwa.gatherin.present.teacher.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.api.RetrofitService
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.model.pack.SaveReceiptReq

import com.orwa.gatherin.model.profile.PacksRes
import com.orwa.gatherin.rep.ProfileRep

import com.skydoves.sandwich.*
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * ViewModel for the [SearchRepositoriesActivity] screen.
 * The ViewModel works with the [GithubRepository] to get the data.
 */
@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    private val rep: ProfileRep
) : BaseNetworkViewModel() {

    private val TAG = SubscriptionsViewModel::class.java.simpleName

    private val _subscriptionsListRes = MutableLiveData<PacksRes?>(null)
    val subscriptionsListRes: LiveData<PacksRes?> = _subscriptionsListRes

    private val _buyPackRes = MutableLiveData<ResponseBody?>(null)
    val buyPackRes: LiveData<ResponseBody?> = _buyPackRes

    private val _saveReceiptRes = MutableLiveData<ResponseBody?>(null)
    val saveReceiptRes: LiveData<ResponseBody?> = _saveReceiptRes

    private val _payPackRes = MutableLiveData<ResponseBody?>(null)
    val payPackRes: LiveData<ResponseBody?> = _payPackRes

    private val _buyPackNetworkState = MutableLiveData(AuthNetworkState.NONE)
    val buyPackNetworkState: LiveData<AuthNetworkState> = _buyPackNetworkState

    fun setBuyPackNetworkState(state: AuthNetworkState){
        _buyPackNetworkState.value = state
    }


    fun getSubscriptionsList() {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.getPackages()
//            _signInRes.value = res
            Log.i(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _subscriptionsListRes.value = data.takeIf { it!=null }
//                Log.i(TAG, "success_res=$data, status_code=$statusCode")
            }

            res.onError {
                Log.e(TAG, "error_res=$statusCode")

                when (statusCode) {
                    StatusCode.Unauthorized -> setNetworkState(AuthNetworkState.USER_UNAUTHORIZED)
                    else -> setNetworkState(AuthNetworkState.FAILURE)
                }
            }
            res.onException {
                setNetworkState(AuthNetworkState.CONNECT_ERROR)

                Log.e(
                    TAG, "EXCEPTION_res=$message"
                )
            }
        }
    }

//    fun buyPackage(packId:Int) {
//        setBuyPackNetworkState(AuthNetworkState.LOADING)
//        viewModelScope.launch {
//            val res = rep.buyPackage(packId)
////            _signInRes.value = res
//            Log.i(TAG, "res=$res")
//            res.onSuccess {
//                setBuyPackNetworkState(AuthNetworkState.SUCCESS)
//                _buyPackRes.value = data
////                Log.i(TAG, "success_res=$data, status_code=$statusCode")
//            }
//
//            res.onError {
////                Log.e(TAG, "error_res=$statusCode")
//
//                when (statusCode) {
//                    StatusCode.Unauthorized -> setBuyPackNetworkState(AuthNetworkState.USER_UNAUTHORIZED)
//                    else -> setBuyPackNetworkState(AuthNetworkState.FAILURE)
//                }
//            }
//            res.onException {
//                setBuyPackNetworkState(AuthNetworkState.CONNECT_ERROR)
//
//                Log.e(
//                    TAG, "EXCEPTION_res=$message"
//                )
//            }
//        }
//    }

    fun saveReceipt(req:SaveReceiptReq) {
        setBuyPackNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.saveReceipt(req)
//            _signInRes.value = res
            Log.i(TAG,"req=$req")
            Log.i(TAG, "res=$res")
            res.onSuccess {
                setBuyPackNetworkState(AuthNetworkState.SUCCESS)
                _saveReceiptRes.value = data
//                Log.i(TAG, "success_res=$data, status_code=$statusCode")
            }

            res.onError {
                Log.e(TAG, "error_res=$statusCode")

                when (statusCode) {
                    StatusCode.Unauthorized -> setBuyPackNetworkState(AuthNetworkState.USER_UNAUTHORIZED)
                    else -> setBuyPackNetworkState(AuthNetworkState.FAILURE)
                }
            }
            res.onException {
                setBuyPackNetworkState(AuthNetworkState.CONNECT_ERROR)

                Log.e(
                    TAG, "EXCEPTION_res=$message"
                )
            }
        }
    }


//    fun payForPackage(token:String,receipt:String) {
//        setBuyPackNetworkState(AuthNetworkState.LOADING)
//        viewModelScope.launch {
//
//            val res = pay(token,receipt)
////            _signInRes.value = res
//            Log.i(TAG, "res=$res")
//            res.onSuccess {
//                setBuyPackNetworkState(AuthNetworkState.SUCCESS)
//                _payPackRes.value = data
////                Log.i(TAG, "success_res=$data, status_code=$statusCode")
//            }
//
//            res.onError {
////                Log.e(TAG, "error_res=$statusCode")
//
//                when (statusCode) {
//                    StatusCode.Unauthorized -> setBuyPackNetworkState(AuthNetworkState.USER_UNAUTHORIZED)
//                    else -> setBuyPackNetworkState(AuthNetworkState.FAILURE)
//                }
//            }
//            res.onException {
//                setBuyPackNetworkState(AuthNetworkState.CONNECT_ERROR)
//
//                Log.e(
//                    TAG, "EXCEPTION_res=$message"
//                )
//            }
//        }
//    }


//    suspend fun pay(token:String, receipt:String):ApiResponse<ResponseBody>{
//
//        val interceptor = HttpLoggingInterceptor()
//            interceptor.level = HttpLoggingInterceptor.Level.BODY
//            val httpClient = OkHttpClient.Builder()
//            httpClient.addInterceptor(interceptor)
//            httpClient.readTimeout(1, TimeUnit.MINUTES)
//                .connectTimeout(1, TimeUnit.MINUTES)
//            httpClient.addInterceptor { chain ->
//                //last_user = user
//                val original = chain.request()
//
//                val request: Request = original.newBuilder()
//                    .header("Accept", "application/ld+json")
//                    .header("Authorization", "Bearer $token")
//                    .method(original.method, original.body)
//                    .build()
////                }
//
////                Log.i(TAG,"request=${request.headers}")
//                chain.proceed(request)
//            }
//            val retrofit = Retrofit.Builder()
//                .baseUrl("http://68.183.132.50:3000/")
//                .addCallAdapterFactory( CoroutinesResponseCallAdapterFactory())
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient.build())
//                .build().create(RetrofitService::class.java)
//
//        return retrofit.pay(receipt)
//
//    }

}
