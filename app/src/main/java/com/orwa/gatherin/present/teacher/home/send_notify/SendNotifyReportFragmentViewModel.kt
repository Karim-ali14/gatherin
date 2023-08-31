package com.orwa.gatherin.present.teacher.home.send_notify

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.base.BaseNetworkViewModel
import com.orwa.gatherin.model.group.AllGroupsResItem
import com.orwa.gatherin.model.notify.SendNotifyReq
import com.orwa.gatherin.rep.HomeRep
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class SendNotifyReportFragmentViewModel @Inject constructor(
    private val rep: HomeRep
) : BaseNetworkViewModel() {

    private val TAG = SendNotifyReportFragmentViewModel::class.java.simpleName

    private val _groupsListRes = MutableLiveData<ArrayList<AllGroupsResItem>?>(null)
    val groupsListRes: LiveData<ArrayList<AllGroupsResItem>?> = _groupsListRes

    private val _sendNotifyRes = MutableLiveData<ResponseBody?>(null)
    val sendNotifyRes: LiveData<ResponseBody?> = _sendNotifyRes

    private val _sendNotifyNetworkState = MutableLiveData(AuthNetworkState.NONE)
    val sendNotifyNetworkState: LiveData<AuthNetworkState> = _sendNotifyNetworkState

    fun getGroupsList() {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch {
            val res = rep.getAllGroups() // TODO: 6/23/2021
//            _signInRes.value = res
//            Log.e(TAG, "res=$res")
            res.onSuccess {
                setNetworkState(AuthNetworkState.SUCCESS)
                _groupsListRes.value = data
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

                Log.e(
                    TAG, "EXCEPTION_res=$message"
                )
            }
        }
    }

    fun sendNotifyReport(req: SendNotifyReq) {
        _sendNotifyNetworkState.value = AuthNetworkState.LOADING
        viewModelScope.launch {
            val res = rep.sendNotify(req) // TODO: 6/23/2021
//            _signInRes.value = res
            Log.e(TAG, "res=$res")
            res.onSuccess {
                _sendNotifyNetworkState.value = AuthNetworkState.SUCCESS
                _sendNotifyRes.value = data
//                Log.i(TAG, "success_res=$data, status_code=$statusCode")
            }

            res.onError {
                Log.e(TAG, "error_res=$statusCode")

                when (statusCode) {
                    StatusCode.Unauthorized -> _sendNotifyNetworkState.value = AuthNetworkState.USER_UNAUTHORIZED
                    else -> _sendNotifyNetworkState.value = AuthNetworkState.FAILURE
                }
            }
            res.onException {
                _sendNotifyNetworkState.value = AuthNetworkState.CONNECT_ERROR

                Log.e(
                    TAG, "EXCEPTION_res=$message"
                )
            }
        }
    }

}