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

package com.orwa.gatherin.present.start

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.model.pack.PackageRes

import com.orwa.gatherin.model.profile.PacksRes
import com.orwa.gatherin.rep.ProfileRep
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * ViewModel for the [SearchRepositoriesActivity] screen.
 * The ViewModel works with the [GithubRepository] to get the data.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val rep: ProfileRep
) : BaseNetworkViewModel() {

    private val TAG = SplashViewModel::class.java.simpleName

//    private val _packStatusRes = MutableLiveData<PackageRes?>(null)
//    val packStatusRes: LiveData<PackageRes?> = _packStatusRes


//    fun checkPackageValidity() {
//        setNetworkState(AuthNetworkState.LOADING)
//        viewModelScope.launch {
//            val res = rep.checkPackage()
////            _signInRes.value = res
//            Log.i(TAG, "res=$res")
//            res.onSuccess {
//                setNetworkState(AuthNetworkState.SUCCESS)
//                _packStatusRes.value = data
////                Log.i(TAG, "success_res=$data, status_code=$statusCode")
//            }
//
//            res.onError {
////                Log.e(TAG, "error_res=$statusCode")
//
//                when (statusCode) {
//                    StatusCode.Unauthorized -> setNetworkState(AuthNetworkState.USER_UNAUTHORIZED)
//                    else -> setNetworkState(AuthNetworkState.FAILURE)
//                }
//            }
//            res.onException {
//                setNetworkState(AuthNetworkState.CONNECT_ERROR)
//
//                Log.e(
//                    TAG, "EXCEPTION_res=$message"
//                )
//            }
//        }
//    }

}
