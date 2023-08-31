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

package com.orwa.gatherin.present.notify

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.present.notify.data.NotifyRepository
import com.orwa.gatherin.db.model.NotifyModel
import com.orwa.gatherin.rep.NotifyRep
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * ViewModel for the [SearchRepositoriesActivity] screen.
 * The ViewModel works with the [NotifyRepository] to get the data.
 */
@ExperimentalPagingApi
@HiltViewModel
class NotifyViewModel @Inject constructor(private val repository: NotifyRepository, private val rep2:NotifyRep) : BaseNetworkViewModel() {
//    private var currentQueryValue: String? = null
    private val TAG = NotifyViewModel::class.java.simpleName

//    private var currentSearchResult: Flow<PagingData<Notification>>? = null

    private val _changeNotificationStateRes = MutableLiveData<ResponseBody?>(null)
    val changeNotificationStateRes: LiveData<ResponseBody?> = _changeNotificationStateRes

    fun setNotificationsStateToRead() {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep2.changeNotificationsState()
//            _signInRes.value = res
//            Log.e(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _changeNotificationStateRes.value = data
//                Log.i(TAG, "success_res=$data, status_code=$statusCode")
            }

            res.onError {
//                Log.e(TAG, "error_res=$statusCode")

                when (statusCode) {
                    StatusCode.Unauthorized -> setNetworkState(AuthNetworkState.USER_UNAUTHORIZED)
                    else -> setNetworkState(AuthNetworkState.FAILURE)
                }
            }
            res.onException {
                setNetworkState(AuthNetworkState.CONNECT_ERROR)

//                Log.e(TAG, "EXCEPTION_res=$message")
            }
        }
    }

    fun searchRepo(): Flow<PagingData<NotifyModel>> {
//        setNetworkState(AuthNetworkState.LOADING)
        val newResult: Flow<PagingData<NotifyModel>> = repository.getNotificationsFlowDb()
            .cachedIn(viewModelScope)

        return newResult
    }
}
